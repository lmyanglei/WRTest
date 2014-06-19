#!/bin/sh
############################################################################3
#
# $Header: fixJDBC-tm4ldaps.sh 12-oct-2004.23:19:12 smendiol Exp $
#
# fixJDBC-tm4ldaps.sh
#
# Copyright (c) 2004, Oracle. All rights reserved.  
#
#    NAME
#      fixJDBC-tm4ldaps.sh - Script to patch classes12 for LDAPS
#
#    DESCRIPTION
#      This script includes two more classes in classes12 by extracting them 
#      from the ojdbc14 driver. These two classes are used to enable support 
#      for LDAPS using a TrustManager with JDK14 in the classes12, and are:
#        a) oracle/net/jndi/TrustManager.class
#        b) oracle/net/jndi/TrustManagerSSLSocketFactory.class
#
#      A third class that is not really part of this fix, but part of 381979
#      would also be copied if necessary. This class is:
#        c) oracle/net/jndi/CustomSSLSocketFactory.class
#
#    NOTES
#      1. Usage: fixJDBC-tm4ldaps arg1 arg2 [arg3]
#
#        arg1 - classes12 driver to fix.
#        arg2 - ojdbc4 driver from which the classes will be extracted.
#        arg3 - destination where the new classes12 driver will be placed.
#
#      2. This last argument is optional, if not defined the original
#         driver defined by arg1 will be over-written. If this is the case
#         a copy of the original with termination "-orig" will be placed
#         in the same directory. If a copy with extension -orig already
#         exists, then it won't be replaced.
#
#      3. If the original driver is a link, and needs to be overwritten,
#         the link is moved to the same name with termination "-orig", and
#         a physical copy of the original file is made.
#
#      4. Example below:
#         $ fixJDBC-tm4ldaps classes12-driver ojdbc14-driver 
#                            [fixed-classes12-destionationFileName]"
#
#      5. Environment variables that can be defined:
#           TMP_FIXFOLDER - tmp directory where the new jar is created
#           JAVA_HOME     - JDK home (fails if not defined)
#
#    MODIFIED   (MM/DD/YY)
#    smendiol    10/12/04 - smendiol_bug-3882238
#    smendiol    10/12/04 - validate that the arguments are regular files 
#    smendiol    10/11/04 - Creation
#
############################################################################3

#-----------------------------------------------------------------------
# Displays this script's description
#-----------------------------------------------------------------------
description()
{
   echo " "
   echo " This scripts patches classes12 JDBC driver to use a TrustManager "
   echo " when the ldaps protocol is used. This is only supported to run  "
   echo " on JDK14. If the third argument is not passed, then the original "
   echo " driver is overwritten. \n"
}

#-----------------------------------------------------------------------
# Displays how to use this script
#-----------------------------------------------------------------------
usage()
{
   echo "Usage: $0 classes12-fileName ojdbc14-fileName [fixed-classes12-destionationFileName]"
}

#-----------------------------------------------------------------------
# Validate number of arguments
#-----------------------------------------------------------------------
validate()
{
  echo " Validating environment and command line arguments ... "

  nArgs="$1"
  arg1="$2"
  arg2="$3"
  arg3="$4"

  if [ "$nArgs" -lt "2" ]
  then
    usage
    exit 1
  fi

  if [ ! "$JAVA_HOME" ]
  then
    echo " Please define the JAVA_HOME env variable where the JDK is installed."
    echo " This script will use \$JAVA_HOME/bin/jar"
    exit 1
  fi
  JAR=$JAVA_HOME/bin/jar

  # Verify that both driver files can be read
  if [ ! -r "$arg1" -o ! -f "$arg1" ]
  then 
    echo " Can not read driver: $arg1"
    exit 1
  fi
  CLASSES12="$arg1"
  
  if [ ! -r "$arg2" -o ! -f "$arg2" ]
  then
    echo " Can not read driver: $arg2"  
    exit 1
  fi
  OJDBC14="$arg2"
  
  # Determine destination file and if it is the same as the original driver
  if [ "$nArgs" -gt "2" ]
  then
    NEWCLASSES12="$arg3"
    if [ $CLASSES12 = $NEWCLASSES12 ]
    then
      OW="Y"
    else
      OW="N"
    fi
  fi
  
  # Give warning.
  if [  "$nArgs" -eq "2"  -o "$OW" = "Y" ]
  then 
    # The original file will be replaced
    NEWCLASSES12="$CLASSES12"
  
    echo "\n WARNING: The original driver will be overwritten. "
    echo " Do you want to overwrite the original driver? (Y/N) [N]:"
    read OW

    # If overwrite is going to happen, then verify write permissions, and 
    # if the file is a link.
    if [ "$OW" ]
    then
      if [ "$OW" = "Y" -o "$OW" = "y" ]
      then
	OW="Y"
        echo "\n Original classes12 JDBC driver will be over written"

	# Check write permissions
        if  [ ! -h "$arg1" -a ! -w "$arg1" ]
        then
          echo " Can not over write file, check file permissions."
          exit 1
        fi

      else 
        if [ "$OW" != "N" -a "$OW" != "n" ]
        then
          echo " Invalid answer, valid answers are: [Y|N]."
        fi
        echo " Please run this script again and define the third argument"
        exit 1
      fi
    else
      echo " Please run this script again and define the third argument"
      exit 1
    fi
  fi
  
  if [ "$OW" != "Y" -a "$OW" != "y" ]
  then
    if [ "$OW" != "N" -a "$OW" != "n" ]
    then
      echo " Found a problem in the script, aborting operation."
      echo "OW = $OW"
      exit 2
    fi
  fi

  # If not defined, set the default
  if [ ! "$TMP_FIXFOLDER" ]
  then
    TMP_FIXFOLDER=/tmp/fix3882238
  fi
}
  
#-----------------------------------------------------------------------
# Set directories, move files and make copies:
#-----------------------------------------------------------------------
setup()
{
  echo " Setting up directory, making copies and backups"

  # Remove tmp directory
  echo "  rm -fr $TMP_FIXFOLDER"
  rm -fr $TMP_FIXFOLDER

  # Create tmp directory
  echo "  mkdir $TMP_FIXFOLDER"
  mkdir $TMP_FIXFOLDER
  mkdir $TMP_FIXFOLDER/classes12
  mkdir $TMP_FIXFOLDER/ojdbc14
  
  if [ "$OW" = "Y" ]
  then
    echo " "

    #  If it is a link, move the link and create a physical version
    if [ -h $NEWCLASSES12 ]
    then
      echo "  -- moving original link to "
      echo "     $arg1-orig "
      mv -f $NEWCLASSES12 $NEWCLASSES12-orig
      echo "  -- making physical copy of original file ... "
      cp $NEWCLASSES12-orig $NEWCLASSES12
      chmod +w $NEWCLASSES12
    else
      # Check if this physical file has already been copied
      if [ ! -f $NEWCLASSES12-orig ]
      then
        echo "  -- a backup of the original will be created"
        echo "  cp $NEWCLASSES12 $NEWCLASSES12-orig"
        cp $NEWCLASSES12 $NEWCLASSES12-orig
      else
        echo "  -- a copy of original driver exists, not copying it again"
      fi
    fi
  fi
 
}

#-----------------------------------------------------------------------
# Unjar original drivers, extract the files and insert them into the patched
# driver. The MANIFEST.MF file will also be modified.
#-----------------------------------------------------------------------
patchDriver()
{
  CURDIR=$PWD

  # VAlidate if this driver contains the fix already
  echo " "
  echo "  cd $TMP_FIXFOLDER/classes12"
  echo "  -- analyzing classes12.jar ... "
  cd $TMP_FIXFOLDER/classes12
  $JAVA_HOME/bin/jar xf $CLASSES12
  PATCH0=`find . -name "CustomSSLSocketFactory.class" -print | grep jndi`
  PATCH1=`find . -name "TrustManager.class" -print | grep jndi`
  PATCH2=`find . -name "TrustManagerSSLSocketFactory.class" -print | grep jndi`
  MANIFEST=`find . -name "MANIFEST.MF" -print`
  if [ "$DEBUG_FIX3882238" ]
  then
    echo "  $JAVA_HOME/bin/jar xf $CLASSES12"
    echo "  TM  : $PATCH1"
    echo "  TMSF: $PATCH2"
    echo "  CSF : $PATCH0"
  fi

  if [ "$PATCH1" -a "$PATCH2" ]
  then
    echo "  -- patch for bug 3882238 has already been applied to driver"
  else

    # Processing ojdbc14
    echo "  cd $TMP_FIXFOLDER/ojdbc14"
    echo "  -- analyzing ojdbc14.jar ... "
    cd $TMP_FIXFOLDER/ojdbc14
    $JAVA_HOME/bin/jar xf $OJDBC14
    TM=`find . -name "TrustManager.class" -print | grep jndi`
    TMSF=`find . -name "TrustManagerSSLSocketFactory.class" -print | grep jndi`
    CSF=`find . -name "CustomSSLSocketFactory.class" -print | grep jndi`
    if [ "$DEBUG_FIX3882238" ]
    then
      echo "  $JAVA_HOME/bin/jar xf $OJDBC14"
      echo "  TM  : $TM"
      echo "  TMSF: $TMSF"
      echo "  CSF : $CSF"
    fi

    #Processing classes12
    echo "\n  -- this driver has not been patched."
    echo "\n Patching driver ... "
    if [ ! "$PATCH0" ]
    then
      cp $TMP_FIXFOLDER/ojdbc14/$CSF  $TMP_FIXFOLDER/classes12/$CSF
    fi
    cp $TMP_FIXFOLDER/ojdbc14/$TM   $TMP_FIXFOLDER/classes12/$TM
    cp $TMP_FIXFOLDER/ojdbc14/$TMSF $TMP_FIXFOLDER/classes12/$TMSF

    echo "  -- updating $MANIFEST file ..."
    MF=$TMP_FIXFOLDER/classes12/$MANIFEST
    cp $MF $TMP_FIXFOLDER

    # Include info into MANIFEST.MF file, as the first line, then insert
    # the existing content
    echo "Extension-Name: Bug 3882238 patch has been applied" >$MF
    while read line
    do 
      echo $line >>$MF
    done < $TMP_FIXFOLDER/MANIFEST.MF
  
    # Jaring new driver with specific MANIFEST.MF
    echo "  -- building new driver ..."
    cd $TMP_FIXFOLDER/classes12
    $JAVA_HOME/bin/jar cfm $NEWCLASSES12 META-INF/MANIFEST.MF javax/ oracle/
  fi

  echo " "
  echo " Driver is fixed for bug 3882238."
  echo "   JDBC Driver: $NEWCLASSES12"

  # return to original directory, so tmp can be cleaned.
  cd $CURDIR
}

#-----------------------------------------------------------------------
# Clean tmp directory
#-----------------------------------------------------------------------
clean()
{
  # Remove tmp directory
  echo "\n Cleaning tmp directory"
  rm -fr $TMP_FIXFOLDER
}


############################################################################3
# Main body for this shell script
############################################################################3

description

validate $# $1 $2 $3

echo " "
echo "  Fixing: $CLASSES12"
echo "    from: $OJDBC14" 
echo "      to: $NEWCLASSES12" 
echo " tempDir: $TMP_FIXFOLDER"
echo "     Jar: $JAR"
echo "\n Press any key to continue ..."
read dummy

setup 

patchDriver

clean
echo " Done. "
