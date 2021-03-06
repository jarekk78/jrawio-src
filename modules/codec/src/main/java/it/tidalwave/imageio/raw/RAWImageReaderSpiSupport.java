/***********************************************************************************************************************
 *
 * jrawio - a Java(TM) Image I/O SPI Provider for Camera Raw files
 * Copyright (C) 2003 - 2016 by Tidalwave s.a.s.
 *
 ***********************************************************************************************************************
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations under the License.
 *
 ***********************************************************************************************************************
 *
 * WWW: http://jrawio.rawdarkroom.org
 * SCM: https://kenai.com/hg/jrawio~src
 *
 **********************************************************************************************************************/
package it.tidalwave.imageio.raw;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.HashMap;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.imageio.spi.ImageReaderSpi;
import javax.imageio.stream.ImageInputStream;
import it.tidalwave.imageio.io.RAWImageInputStream;
import it.tidalwave.imageio.io.RAWImageInputStreamImpl;
import it.tidalwave.imageio.util.Logger;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
public abstract class RAWImageReaderSpiSupport extends ImageReaderSpi
  {
    private final static String CLASS = RAWImageReaderSpiSupport.class.getName();
    private final static Logger logger = Logger.getLogger(CLASS);

    /** A postprocessor, if available, will be run against the loaded image. */
    private static Map<Class<?>, PostProcessor> postProcessorMapBySpiClass = new HashMap<Class<?>, PostProcessor>();
    
    /*******************************************************************************************************************
     * 
     * @param names
     * @param suffixes
     * @param MIMETypes
     * @param readerClass
     * @param inputTypes
     * @param writerSpiNames
     * @param supportsStandardStreamMetadataFormat
     * @param nativeStreamMetadataFormatName
     * @param nativeStreamMetadataFormatClassName
     * @param extraStreamMetadataFormatNames
     * @param extraStreamMetadataFormatClassNames
     * @param supportsStandardImageMetadataFormat
     * @param nativeImageMetadataFormatName
     * @param nativeImageMetadataFormatClassName
     * @param extraImageMetadataFormatNames
     * @param extraImageMetadataFormatClassNames
     * 
     ******************************************************************************************************************/
    protected RAWImageReaderSpiSupport (String[] names, String[] suffixes, String[] MIMETypes, Class readerClass,
                                        Class[] inputTypes, String[] writerSpiNames,
                                        boolean supportsStandardStreamMetadataFormat,
                                        String nativeStreamMetadataFormatName,
                                        String nativeStreamMetadataFormatClassName,
                                        String[] extraStreamMetadataFormatNames,
                                        String[] extraStreamMetadataFormatClassNames,
                                        boolean supportsStandardImageMetadataFormat,
                                        String nativeImageMetadataFormatName,
                                        String nativeImageMetadataFormatClassName,
                                        String[] extraImageMetadataFormatNames,
                                        String[] extraImageMetadataFormatClassNames)
      {
        super("tidalwave.it", Version.TAG, names, suffixes, MIMETypes, readerClass.getName(), inputTypes,
            writerSpiNames, supportsStandardStreamMetadataFormat, nativeStreamMetadataFormatName,
            nativeStreamMetadataFormatClassName, extraStreamMetadataFormatNames, extraStreamMetadataFormatClassNames,
            supportsStandardImageMetadataFormat, nativeImageMetadataFormatName, nativeImageMetadataFormatClassName,
            extraImageMetadataFormatNames, extraImageMetadataFormatClassNames);
      }

    /*******************************************************************************************************************
     * 
     * @param name
     * @param suffixes
     * @param mimeType
     * @param readerClass
     * 
     *******************************************************************************/
    protected RAWImageReaderSpiSupport (String name, String[] suffixes, String mimeType, Class readerClass)
      {
        this(new String[] { name.toLowerCase(), name.toUpperCase() }, 
            suffixes, 
            new String[] { mimeType }, 
            readerClass, 
            new Class[] { ImageInputStream.class }, // inputTypes
            null, // writerSpiNames
            false, // supportsStandardStreamMetadataFormat
            null, // nativeStreamMetadataFormatName
            null, // nativeStreamMetadataFormatClassName
            null, // extraStreamMetadataFormatNames
            null, // extraStreamMetadataFormatClassNames
            false, // supportsStandardImageMetadataFormat
            null, // nativeImageMetadataFormatName
            null, // nativeImageMetadataFormatClassName
            null, // extraImageMetadataFormatNames
            null); // extraImageMetadataFormatClassNam
      }

    /*******************************************************************************************************************
     * 
     * @param name
     * @param suffix
     * @param mimeType
     * @param readerClass
     * 
     *******************************************************************************/
    protected RAWImageReaderSpiSupport (String name, String suffix, String mimeType, Class readerClass)
      {
        this(name, new String[] { suffix.toLowerCase(), suffix.toUpperCase() }, mimeType, readerClass);
      }

    /*******************************************************************************************************************
     * 
     * Installs a postprocessor that will be run against all the instances of images
     * loaded by this Spi.
     * 
     * @param postProcessor  the post processor to install
     *
     *******************************************************************************/
    public static void installPostProcessor (Class<?> spiClass, PostProcessor postProcessor)
      {
        postProcessorMapBySpiClass.put(spiClass, postProcessor);
      }

    /*******************************************************************************************************************
     * 
     * Post-processes a raw image using the installed postprocessor, if any.
     *
     * @param  image  the raw image to postprocess
     * @return        the post-processed image
     * 
     ******************************************************************************************************************/
    @Nonnull
    protected BufferedImage postProcess (final @Nonnull BufferedImage image,
                                         final @Nonnull RAWMetadataSupport metadata,
                                         final @Nonnull RAWImageReadParam readParam)
      {
        logger.fine("postProcess(%s, %s, %s)", image, metadata.getClass(), readParam);
        final Source source = readParam.lookup(Source.class);
        final PostProcessor postProcessor = !source.needsPostProcessor() ? null : postProcessorMapBySpiClass.get(getClass());
        logger.finer(">>>> source: %s, postProcessor: %s", source, postProcessor);
        
        return (postProcessor != null) ? postProcessor.process(image, metadata, readParam) : image;
      }
    
    /*******************************************************************************************************************
     *
     * Post-processes a raw image using the installed postprocessor, if any.
     *
     * @param  image  the raw image to postprocess
     * @return        the post-processed image
     *
     ******************************************************************************************************************/
    protected void postProcessMetadata (final @Nonnull RAWMetadataSupport metadata,
                                        final @Nonnull RAWImageReadParam readParam)
      {
        logger.fine("postProcessMetadata(%s, %s)", metadata.getClass(), readParam);
        final Source source = readParam.lookup(Source.class);
        final PostProcessor postProcessor = !source.needsPostProcessor() ? null : postProcessorMapBySpiClass.get(getClass());
        logger.finer(">>>> source: %s, postProcessor: %s", source, postProcessor);

        if (postProcessor != null)
          {
            postProcessor.processMetadata(metadata, readParam);
          }
      }

    /*******************************************************************************************************************
     * 
     * @inheritDoc
     * 
     ******************************************************************************************************************/
    public boolean canDecodeInput (Object source)
      throws IOException
      {
        if (source instanceof ImageInputStream)
          {
            return canDecodeInput((ImageInputStream)source);
          }

        else
          {
            ImageInputStream iis = null;
            
            try
              {
                iis = ImageIO.createImageInputStream(source);
                
                if (iis != null)
                  {
                    return canDecodeInput(iis);
                  }
              }
            
            finally
              {
                if (iis != null)
                  {
                    iis.close();
                  }
              }
          }

        return false;
      }

    /*******************************************************************************************************************
     * 
     * 
     ******************************************************************************************************************/
    private boolean canDecodeInput (ImageInputStream source) throws IOException
      {
        RAWImageInputStream iis = new RAWImageInputStreamImpl(source);
        iis.setDontCloseDelegate(); // otherwise the garbage collector will close it together with the original source!!

        try
          {
            iis.mark();
            return canDecodeInput(iis);
          }

        catch (Exception e)
          {
            return false;
          }

        finally
          {
            iis.setBaseOffset(0);
            iis.reset();
          }
      }
    
    /*******************************************************************************************************************
     * 
     * @param iis
     * @return
     * @throws IOException
     * 
     ******************************************************************************************************************/
    protected abstract boolean canDecodeInput (RAWImageInputStream iis) throws IOException;
  }
