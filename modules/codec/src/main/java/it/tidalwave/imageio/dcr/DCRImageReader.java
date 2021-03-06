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
package it.tidalwave.imageio.dcr;

import it.tidalwave.imageio.tiff.TIFFMetadataSupport;
import it.tidalwave.imageio.util.Logger;
import java.io.IOException;
import javax.imageio.spi.ImageReaderSpi;
import java.awt.image.WritableRaster;
import it.tidalwave.imageio.io.RAWImageInputStream;
import it.tidalwave.imageio.raw.RasterReader;
import it.tidalwave.imageio.tiff.IFD;
import it.tidalwave.imageio.tiff.TIFFImageReaderSupport;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
public class DCRImageReader extends TIFFImageReaderSupport
  {
    private final static Logger logger = Logger.getLogger("it.tidalwave.imageio.dcr.DCRImageReader");

    /*******************************************************************************************************************
     *
     ******************************************************************************************************************/
    protected DCRImageReader (ImageReaderSpi originatingProvider, Object extension)
      {
        super(originatingProvider, KodakMakerNote.class, DCRMetadata.class);
      }

    /*******************************************************************************************************************
     *
     * @inheritDoc
     * 
     * Loads the NEF raster delegating to the appropriate NEFPixelLoader subclass.
     * 
     ******************************************************************************************************************/
    protected WritableRaster loadRAWRaster() throws IOException
      {
        logger.fine("loadRAWRaster() - iis: %s", iis);

        long time = System.currentTimeMillis();
        DCRRasterReader rasterReader = new DCRRasterReader();
        initializeRasterReader(rasterReader);

        logger.finest(">>>> using rasterReader: %s", rasterReader);
        IFD primaryIFD = (IFD)primaryDirectory;
        iis.seek(primaryIFD.getStripOffsets()); // FIXME: set attribute in raster reader, seek done in rasterreader
        WritableRaster raster = rasterReader.loadRaster(iis, this);
        logger.finer(">>>> loadRAWRaster() completed ok in %d msec", (System.currentTimeMillis() - time));

        return raster;
      }

    /*******************************************************************************************************************
     * 
     * FIXME: merge with superclass
     * 
     * @param rasterReader
     * 
     *******************************************************************************/
    protected void initializeRasterReader (RasterReader rasterReader)
      {
        IFD primaryIFD = (IFD)primaryDirectory;
        IFD exifIFD = (IFD)primaryIFD.getNamedDirectory(IFD.EXIF_NAME);
        IFD rasterIFD = ((TIFFMetadataSupport)metadata).getRasterIFD();
        
        int bitsPerSample =  primaryIFD.getBitsPerSample()[0]; 
        int width = rasterIFD.getImageWidth();
        int height = rasterIFD.getImageLength();
        rasterReader.setWidth(width);
        rasterReader.setHeight(height);
        rasterReader.setBitsPerSample(bitsPerSample);
        rasterReader.setCFAPattern(((DCRMetadata)metadata).getRasterIFD().getCFAPattern());
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
