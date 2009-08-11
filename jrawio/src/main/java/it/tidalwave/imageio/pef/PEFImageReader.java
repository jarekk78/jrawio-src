/***********************************************************************************************************************
 *
 * jrawio - a Java(TM) Image I/O SPI Provider for Camera Raw files
 * ===============================================================
 *
 * Copyright (C) 2003-2009 by Tidalwave s.a.s. (http://www.tidalwave.it)
 * http://jrawio.tidalwave.it
 *
 ***********************************************************************************************************************
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License. 
 *
 ***********************************************************************************************************************
 *
 * $Id: PEFImageReader.java 156 2008-09-13 18:39:08Z fabriziogiudici $
 *
 **********************************************************************************************************************/
package it.tidalwave.imageio.pef;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import it.tidalwave.imageio.util.Logger;
import java.io.IOException;
import java.awt.image.WritableRaster;
import javax.imageio.spi.ImageReaderSpi;
import it.tidalwave.imageio.raw.RasterReader;
import it.tidalwave.imageio.tiff.IFD;
import it.tidalwave.imageio.tiff.TIFFImageReaderSupport;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id: PEFImageReader.java 156 2008-09-13 18:39:08Z fabriziogiudici $
 *
 **********************************************************************************************************************/
public class PEFImageReader extends TIFFImageReaderSupport
  {
    private final static String CLASS = PEFImageReader.class.getName();
    private final static Logger logger = Logger.getLogger(CLASS);

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    protected PEFImageReader (@Nonnull final ImageReaderSpi originatingProvider, 
                              @CheckForNull final Object extension)
      {
        super(originatingProvider, PentaxMakerNote.class, PEFMetadata.class);
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     * 
     ******************************************************************************************************************/
    @Nonnull
    protected WritableRaster loadRAWRaster() 
      throws IOException
      {
        logger.fine("loadRAWRaster() - iis: %s", iis);

        final long time = System.currentTimeMillis();
        final PEFRasterReader rasterReader = new PEFRasterReader();
        initializeRasterReader(rasterReader);

        logger.finest(">>>> using rasterReader: %s", rasterReader);
        final IFD primaryIFD = (IFD)primaryDirectory;
        iis.seek(primaryIFD.getStripOffsets()); // FIXME: set attribute in raster reader, seek done in rasterreader
        final WritableRaster raster = rasterReader.loadRaster(iis, this);
        logger.finer(">>>> loadRAWRaster() completed ok in %d msec.", (System.currentTimeMillis() - time));

        return raster;
      }

    /*******************************************************************************************************************
     * 
     * FIXME: merge with superclass
     *
     * {@inheritDoc}
     * 
     ***************************************************************************/
    protected void initializeRasterReader (@Nonnull final RasterReader rasterReader)
      {
        final IFD primaryIFD = (IFD)primaryDirectory;
        final IFD exifIFD = (IFD)primaryIFD.getNamedDirectory(IFD.EXIF_NAME);
        
        final int bitsPerSample = (primaryIFD.getCompression().intValue() == 1) ? 16 : 12; // packed in words primaryIFD.getBitsPerSample()[0];
        final int width = primaryIFD.getImageWidth();
        final int height = primaryIFD.getImageLength();
        rasterReader.setWidth(width);
        rasterReader.setHeight(height);
        rasterReader.setBitsPerSample(bitsPerSample);
        rasterReader.setCFAPattern(exifIFD.getEXIFCFAPattern());
        rasterReader.setCompression(primaryIFD.getCompression().intValue());

        if (primaryIFD.isStripByteCountsAvailable())
          {
            rasterReader.setStripByteCount(primaryIFD.getStripByteCounts());
          }

        if (primaryIFD.isTileWidthAvailable())
          {
            int tileWidth = primaryIFD.getTileWidth();
            int tileLength = primaryIFD.getTileLength();
            rasterReader.setTileWidth(tileWidth);
            rasterReader.setTileHeight(tileLength);
            rasterReader.setTilesAcross((width + tileWidth - 1) / tileWidth);
            rasterReader.setTilesDown((height + tileLength - 1) / tileLength);
            rasterReader.setTileOffsets(primaryIFD.getTileOffsets());
            //int[] tileByteCounts = primaryIFD.getTileByteCounts();
          }

        if (primaryIFD.isLinearizationTableAvailable())
          {
            rasterReader.setLinearizationTable(primaryIFD.getLinearizationTable());
          }
      }
  }
