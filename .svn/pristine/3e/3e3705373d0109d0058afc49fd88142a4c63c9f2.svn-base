/**
 *
 */

/*
 * IMPORTANT NOTICE
 *
 * Please note that any and all title and/or intellectual property rights in and
 * to
 * this Software or any part of this (including without limitation any images,
 * photographs, animations, video, audio, music, text and/or "applets,"
 * incorporated
 * into the Software), herein mentioned to as "Software", the accompanying
 * printed
 * materials, and any copies of the Software, are owned by Jataayu Software (P)
 * Ltd.,
 * Bangalore ("Jataayu") or Jataayu's suppliers as the case may be. The Software
 * is
 * protected by copyright, including without limitation by applicable copyright
 * laws,
 * international treaty provisions, other intellectual property laws and
 * applicable
 * laws in the country in which the Software is being used.
 * You shall not modify, adapt or translate the Software, without prior express
 * written consent from Jataayu. You shall not reverse engineer, decompile,
 * disassemble or otherwise alter the Software, except and only to the extent
 * that such activity is expressly permitted by applicable law notwithstanding
 * this limitation. Unauthorized reproduction or redistribution of this program
 * or any portion of it may result in severe civil and criminal penalties and
 * will be prosecuted to the maximum extent possible under the law.
 * Jataayu reserves all rights not expressly granted.
 *
 * THIS SOFTWARE IS PROVIDED TO YOU "AS IS" WITHOUT WARRANTY OF ANY KIND AND ANY
 * AND ALL REPRESENTATION AND WARRANTIES, EITHER EXPRESS OR IMPLIED, INCLUDING
 * BUT NOT LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY ACCURACY OF
 * INFORMATIONAL CONTENT, AND/OR FITNESS FOR A PARTICULAR PURPOSE OR USE, TITLE
 * OR INFRINGEMENT ARE EXPRESSLY DISCLAIMED TO THE FULLEST EXTENT PERMITTED BY
 * LAW.
 * YOU ASSUME THE ENTIRE RISK AS TO THE ACCURACY AND THE USE OF THIS SOFTWARE.
 * JATAAYU SHALL NOT BE LIABLE FOR ANY CONSEQUENTIAL, INCIDENTAL, INDIRECT,
 * EXEMPLARY, SPECIAL OR PUNITIVE DAMAGES INCLUDING WITHOUT LIMITATION ANY LOSS
 * OF DATA, OR; LOSS OF PROFIT, SAVINGS BUSINESS OR GOODWILL OR OTHER SIMILAR
 * LOSS
 * RESULTING FROM OR OUT OF THE USE OR INABILITY TO USE THIS SOFTWARE, EVEN IF
 * JATAAYU HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGE, OR FOR ANY CLAIM
 * BY
 * ANY THIRD PARTY.
 */
/*
 * Program Name : GenerateSmsPushCDR.java
 * Date Created : Aug 19, 2008
 * Created By : Amit Joshi
 * Description :
 */
package com.comviva.api.utils;

import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

public class WriteEDR {

    private static final Logger LOGGER = Logger.getLogger(WriteEDR.class);
    private  static   WriteEDR writeEDR;
    private final String edrGenerationFolderPath;
    private final String fileName;
    private final int totalRecordsPerFile;
    private final long sizeForFileRotation;
    private long edrNoOfLines = 0L;
    private long edrFileSize = 0L;
    private File edrFileForWritting;
    private FileOutputStream edrfos;
    private String headerObject;
    private String line;

    public WriteEDR(String fileName, String edrGenerationFolderPath, int totalRecordsPerFile, long sizeForFileRotation) {
        this.edrGenerationFolderPath = edrGenerationFolderPath;
        this.fileName = fileName;
        this.totalRecordsPerFile = totalRecordsPerFile;
        this.sizeForFileRotation = sizeForFileRotation;
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("File Properties | EdrGenerationFolderPath : " + edrGenerationFolderPath + " | File Name : " + fileName + " | TotalRecordsPerFile : " + totalRecordsPerFile + " | SizeForFileRotation : " + sizeForFileRotation);
        }
        checkDirectoryExistsAndCreate(edrGenerationFolderPath);
        edrFileForWritting = new File(edrGenerationFolderPath + File.separator + fileName);
    }

    /**
     * Construtor for CampaignActionStatus which writes headers if header is
     * set..
     *
     * @param fileName
     * @param edrGenerationFolderPath
     * @param totalRecordsPerFile
     * @param sizeForFileRotation
     * @param headerObject
     */
    public WriteEDR(String fileName, String edrGenerationFolderPath, int totalRecordsPerFile, long sizeForFileRotation, String headerObject) {
        this.edrGenerationFolderPath = edrGenerationFolderPath;
        this.fileName = fileName;
        this.totalRecordsPerFile = totalRecordsPerFile;
        this.sizeForFileRotation = sizeForFileRotation;
        this.headerObject = headerObject;

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("File Properties | EdrGenerationFolderPath : " + edrGenerationFolderPath + " | File Name : " + fileName + " | TotalRecordsPerFile : " + totalRecordsPerFile + " | SizeForFileRotation : " + sizeForFileRotation + " | HeaderObject :" + headerObject);
        }
        checkDirectoryExistsAndCreate(edrGenerationFolderPath);
        edrFileForWritting = new File(edrGenerationFolderPath + File.separator + fileName);
        if (null != this.headerObject && !this.headerObject.isEmpty()) {
            writeHeader();
        }
    }

    public static WriteEDR getInstance(String edrFileName, String edrGenerationFolderPath2, int totalRecordsPerFile2, long sizeForFileRotation2, String headerObject) {
        if (writeEDR == null) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("WriteEDR Object null...creating a new object...");
            }
            writeEDR = new WriteEDR(edrFileName, edrGenerationFolderPath2, totalRecordsPerFile2, sizeForFileRotation2, headerObject);
        }
        return writeEDR;
    }

    public static WriteEDR getInstance(String edrFileName, String edrGenerationFolderPath2, int totalRecordsPerFile2, long sizeForFileRotation2) {
        if (writeEDR == null) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("WriteEDR Object null...creating a new object...");
            }
            writeEDR = new WriteEDR(edrFileName, edrGenerationFolderPath2, totalRecordsPerFile2, sizeForFileRotation2);
        }
        return writeEDR;
    }

    /**
     * @param edrList
     */
    public void generateEDR(List<String> edrList) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("File Properties | EdrGenerationFolderPath : " + edrGenerationFolderPath + " | File Name : " + fileName + " | TotalRecordsPerFile : " + totalRecordsPerFile + " | SizeForFileRotation : " + sizeForFileRotation);
        }
        StringBuffer contents = new StringBuffer();
        long numberOfRecords = 0;
        long fileSize = 0L;
        if (edrNoOfLines == 0) {
            numberOfRecords = getNumberOfRecordsFromFile(edrFileForWritting, edrGenerationFolderPath);
            edrNoOfLines = numberOfRecords;
            edrFileSize = edrFileForWritting.length();
            LOGGER.debug("Existing file properties | EdrNoOfLines : " + edrNoOfLines + " | EdrFileSize : " + edrFileSize);
        } else {
            numberOfRecords = edrNoOfLines;
        }
        fileSize = edrFileSize;

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Writing EDR into file ");
        }

        // Case-1 :: Already file exists and few records are there.
        if (fileSize >= sizeForFileRotation || numberOfRecords == totalRecordsPerFile) {

            // Close the Existing file Stream.
            if (edrfos != null) {
                try {
                    edrfos.close();
                } catch (IOException e) {
                    LOGGER.debug("The IOexception: ", e);
                    edrfos = null;
                }
            }

            File fileToRename = new File(edrGenerationFolderPath + File.separator + makeFileExtension());
            if (edrFileForWritting.renameTo(fileToRename)){
            contents = new StringBuffer();
            numberOfRecords = 0;
            // edrFileForWritting = new File(edrGenerationFolderPath+
            // File.separator + fileName);
            edrfos = reCreatingFileOutPutStream(edrfos, edrFileForWritting);
            edrFileSize = 0;
            edrNoOfLines = 0;
            fileSize = 0;

            // Writes header to the new file created...if and only if header
            // object is not null and empty
            /*
             * if(null!=this.headerObject || !this.headerObject.isEmpty()) {
             * writeHeader(); }
             */
        }
        }

        if (fileSize >= 0 && fileSize < sizeForFileRotation || numberOfRecords >= 0 && numberOfRecords < totalRecordsPerFile) {
            try {
                int listSize = edrList.size();
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("The EDR List Size is " + listSize);
                }
                byte[] fileRecordsInByte = null;
                int listCount = 0;
                while (listCount < listSize) {
                    contents = new StringBuffer();
                    contents.append(edrList.get(listCount));
                    contents.append(System.getProperty("line.separator"));
                    fileRecordsInByte = contents.toString().getBytes();
                    writeToFile(edrFileForWritting, fileRecordsInByte, edrfos);
                    numberOfRecords++;
                    fileSize = fileSize + fileRecordsInByte.length;
                    edrNoOfLines = numberOfRecords;
                    edrFileSize = fileSize;
                    if (fileSize >= sizeForFileRotation || numberOfRecords == totalRecordsPerFile) {
                        LOGGER.debug("Properties while writing : fileSize " + fileSize + " | numberOfRecords :" + numberOfRecords);
                        if (edrfos != null) {
                            try {
                                edrfos.close();
                            } catch (IOException e) {
                                LOGGER.debug("The IOexception: ", e);
                                edrfos = null;
                            }
                        }

                        File fileToRename = new File(edrGenerationFolderPath + File.separator + makeFileExtension());
                        if(edrFileForWritting.renameTo(fileToRename)){
                        contents = new StringBuffer();
                        numberOfRecords = 0;
                        edrFileForWritting = new File(edrGenerationFolderPath + File.separator + fileName);
                        edrfos = reCreatingFileOutPutStream(edrfos, edrFileForWritting);
                        edrFileSize = 0;
                        edrNoOfLines = 0;

                        fileSize = 0;

                        /*
                         * if(null!=this.headerObject ||
                         * !this.headerObject.isEmpty()) { writeHeader(); }
                         */
                    }
                    }
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("number of records after writing :" + numberOfRecords);
                    }

                    listCount++;
                }

            } catch (IOException ex) {
                LOGGER.debug("The IOexception: ", ex);
                checkDirectoryExistsAndCreate(edrGenerationFolderPath);
                LOGGER.fatal("Unable to Write EDR :" + contents.toString());
                edrfos = reCreatingFileOutPutStream(edrfos, edrFileForWritting);
            } catch (NumberFormatException ex) {
                LOGGER.debug("The number format exception is: ", ex);
            } finally {
                // Clear the list to release the memory by GC.
                edrList.clear();
                edrList = null;
            }
        }
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Lines in the EDR file : " + edrNoOfLines + " Size of the EDR file : " + edrFileSize);

        }
    }

    private void writeHeader() {
        byte[] fileRecordsInByte = null;
        StringBuffer contents = new StringBuffer();
        contents.append(this.headerObject);
        contents.append(System.getProperty("line.separator"));
        fileRecordsInByte = contents.toString().getBytes();

        try {
            writeToFile(this.edrFileForWritting, fileRecordsInByte, this.edrfos);
            this.edrNoOfLines++;
            this.edrFileSize = this.edrFileSize + fileRecordsInByte.length;
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("INFO :: Header written to the file...");
                LOGGER.info("INFO :: TotalFileSize :: " + this.edrFileSize);
                LOGGER.info("INFO :: TotalNumberOfRecords in file ::" + this.edrNoOfLines);
            }
        } catch (IOException e) {
            LOGGER.debug("ERROR :: Exception raised while writing header to the file...", e);
        }

    }

    private synchronized void writeToFile(File outFile, byte[] fileData, FileOutputStream fos) throws IOException {
        if (!outFile.exists() && outFile.createNewFile()) {
            
            
           // outFile.createNewFile();
            fos = new FileOutputStream(outFile, true);
        }
        if (fos == null) {
            fos = new FileOutputStream(outFile, true);
        }
        try {
            fos.write(fileData);
            fos.flush();
        } catch (Exception e) {
            LOGGER.error("Exception while writting EDR to File", e);
            fos = reCreatingFileOutPutStream(fos, outFile);
        }
        finally {
            fos.close();
          }

    }

    private String makeFileExtension() {
        long timeInmillis = System.currentTimeMillis();

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeInmillis);

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        month++;
        int day = calendar.get(Calendar.DATE);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);

        StringBuffer fileExtensionCreation = new StringBuffer();
        fileExtensionCreation.append(fileName);
        fileExtensionCreation.append(".");
        // Append Year
        fileExtensionCreation.append(year);
        // Append Month
        if (month <= 9) {
            fileExtensionCreation.append("0");
        }
        fileExtensionCreation.append(month);
        // Append Day
        if (day <= 9) {
            fileExtensionCreation.append("0");
        }
        fileExtensionCreation.append(day);

        fileExtensionCreation.append(".");
        // Append Hour
        if (hour <= 9) {
            fileExtensionCreation.append("0");
        }
        fileExtensionCreation.append(hour);
        // Append Minute
        if (minute <= 9) {
            fileExtensionCreation.append("0");
        }
        fileExtensionCreation.append(minute);

        // Append Seconds
        if (second <= 9) {
            fileExtensionCreation.append("0");
        }
        fileExtensionCreation.append(second);

        return fileExtensionCreation.toString();
    }

    private long getNumberOfRecordsFromFile(File fileForWriting, String path) {
        long totalrecords = 0;
        BufferedReader input = null;
        try {
            input = new BufferedReader(new FileReader(fileForWriting));
            line = null;
            while (( line = input.readLine()) != null) {
                totalrecords++;
            }
        } catch (IOException ex) {
            LOGGER.error("The io exception in existing file: ", ex);
            checkDirectoryExistsAndCreate(path);
        } finally {
            try {
                if (input != null) {
                    input.close();
                }
            } catch (IOException e) {
                LOGGER.error("Exception while closing file", e);
            }
        }

        return totalrecords;
    }

    private boolean checkDirectoryExistsAndCreate(String path) {
        LOGGER.info("Checking Directory exists or not for , if not exists will create PATH:" + path);
        boolean flag = false;
        if (path != null) {
            try {
                File fileForWriting = new File(path);
                if (!fileForWriting.exists()) {
                    flag = fileForWriting.mkdirs();
                    LOGGER.info("Created Directory :" + flag);
                } else {
                    flag = true;
                }
                if (flag && fileForWriting.canRead() && fileForWriting.canWrite()) {
                    flag = true;
                } else {
                    LOGGER.info("Permissions not available to acces file Read:" + fileForWriting.canRead() + " Write:" + fileForWriting.canWrite());
                    flag = false;
                }
            } catch (Exception e) {
                LOGGER.error("Exception while Creating EDR Directories , ", e);
            }

        }
        return flag;

    }

    private synchronized FileOutputStream reCreatingFileOutPutStream(FileOutputStream fos, File fileForWriting) {
        try {
            if (fos != null) {
                fos.close();
            }
            fos = new FileOutputStream(fileForWriting, true);
        } catch (IOException e) {
            LOGGER.error("Exception while reCreatingFileOutPutStream, ", e);
        }
        return fos;
    }
}