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
package it.tidalwave.imageio.cr2;

import java.awt.image.WritableRaster;
import java.io.IOException;
import javax.imageio.spi.ImageReaderSpi;
import it.tidalwave.imageio.tiff.IFD;
import it.tidalwave.imageio.tiff.TIFFImageReaderSupport;
import it.tidalwave.imageio.util.Logger;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
public class CR2ImageReader extends TIFFImageReaderSupport
  {
    private final static Logger logger = Logger.getLogger("it.tidalwave.imageio.crw.CR2ImageReader");

    /*******************************************************************************************************************
     * 
     * @param originatingProvider
     * 
     *******************************************************************************/
    protected CR2ImageReader (ImageReaderSpi originatingProvider)
      {
        super(originatingProvider, CanonCR2MakerNote.class, CR2Metadata.class);
      }

    /*******************************************************************************************************************
     * 
     * @inheritDoc
     * 
     *******************************************************************************/
    protected WritableRaster loadRAWRaster() 
      throws IOException
      {
        logger.fine("loadRAWRaster() - iis: %s", iis);
        long time = System.currentTimeMillis();
        
        final CR2RasterReader rasterReader = new CR2RasterReader();
        final IFD rasterIFD = ((CR2Metadata)metadata).getRasterIFD();
        final CanonCR2MakerNote cr2MakerNote = (CanonCR2MakerNote)makerNote;
        final CR2SensorInfo sensorInfo = cr2MakerNote.getSensorInfo();
        rasterReader.setWidth(sensorInfo.getWidth());
        rasterReader.setHeight(sensorInfo.getHeight());
        rasterReader.setBitsPerSample(12); // FIXME - gets from the model
        rasterReader.setCFAPattern(new byte[] { 0, 1, 1, 2 }); // FIXME RGGB - gets from the model
        iis.seek(rasterIFD.getStripOffsets());
        rasterReader.setStripByteCount(rasterIFD.getStripByteCounts()); 
        rasterReader.setCompression(rasterIFD.getCompression().intValue()); 
        
        if (rasterIFD.isCanonTileInfoAvailable())
          {
            int[] tileInfo = rasterIFD.getCanonTileInfo();
            rasterReader.setCanonTileWidth(tileInfo[1]);
            rasterReader.setCanonLastTileWidth(tileInfo[2]);
          }
        
        WritableRaster raster = rasterReader.loadRaster(iis, this);
        logger.finer(">>>> loadRAWRaster() completed ok in %d msec", (System.currentTimeMillis() - time));
        
        return raster;
      }
  }
