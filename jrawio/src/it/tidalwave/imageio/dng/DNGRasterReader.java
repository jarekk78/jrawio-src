/*******************************************************************************
 *
 * jrawio - a Java(TM) ImageIO API Spi Provider for RAW files
 * ==========================================================
 *
 * Copyright (C) 2003-2006 by Fabrizio Giudici (Fabrizio.Giudici@tidalwave.it)
 * Project home page: http://jrawio.tidalwave.it
 *
 *******************************************************************************
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
 *******************************************************************************
 *
 * $Id: DNGRasterReader.java 9 2006-11-28 12:43:27Z fabriziogiudici $
 *
 ******************************************************************************/

package it.tidalwave.imageio.dng;

import java.util.logging.Logger;
import java.io.IOException;
import java.nio.ByteOrder;
import java.awt.image.DataBufferUShort;
import java.awt.image.WritableRaster;
import it.tidalwave.imageio.decoder.LosslessJPEGDecoder;
import it.tidalwave.imageio.io.RAWImageInputStream;
import it.tidalwave.imageio.raw.RAWImageReaderSupport;
import it.tidalwave.imageio.raw.RasterReader;
import it.tidalwave.imageio.tiff.IFD;

/*******************************************************************************
 *
 * @author  fritz
 * @version CVS $Id: DNGRasterReader.java 9 2006-11-28 12:43:27Z fabriziogiudici $
 *
 ******************************************************************************/
public class DNGRasterReader extends RasterReader
  {
    private static final int BUFFER_SIZE = 128 * 1024;

    private final static Logger logger = Logger.getLogger("it.tidalwave.imageio.dng.DNGRasterReader");

    /*******************************************************************************
     * 
     * @inheritDoc
     * 
     *******************************************************************************/
    protected boolean isCompressedRaster ()
      {
        return compression == IFD.Compression.JPEG.intValue();
      }

    /*******************************************************************************
     * 
     * @inheritDoc
     * 
     *******************************************************************************/
    protected void loadCompressedRaster (RAWImageInputStream iis,
                                         WritableRaster raster,
                                         RAWImageReaderSupport ir) throws IOException
      {
        logger.finest(">>>> tileSize:    " + tileWidth + " x " + tileHeight);
        logger.finest(">>>> tileCount:   " + tilesAcross + " x " + tilesDown);
        ByteOrder byteOrderSaved = iis.getByteOrder();
        iis.setByteOrder(ByteOrder.BIG_ENDIAN);
        DataBufferUShort dataBuffer = (DataBufferUShort)raster.getDataBuffer();
        short[] data = dataBuffer.getData();
        int width = raster.getWidth();
        int height = raster.getHeight();
        int pixelStride = 3; // FIXME
        int scanStride = width * pixelStride;
        int tileIndex = 0;

        LosslessJPEGDecoder jpegDecoder = new LosslessJPEGDecoder();
        iis.selectBitReader(-1, 0); 
        iis.setSkipZeroAfterFF(true); 

        for (int tileRow = 0; tileRow < tilesDown; tileRow++)
          {
            for (int tileColumn = 0; tileColumn < tilesAcross; tileColumn++)
              {
                iis.seek(tileOffsets[tileIndex]);
                jpegDecoder.reset(iis);

                for (int tileY = 0; tileY < tileHeight; tileY++)
                  {
                    short[] rowBuffer = jpegDecoder.loadRow(iis);
                    int y = tileRow * tileHeight + tileY;

                    if (y >= height)
                      {
                        break;
                      }

                    for (int tileX = 0; tileX < tileWidth; tileX++)
                      {
                        int x = tileColumn * tileWidth + tileX;

                        if (x >= width)
                          {
                            break;
                          }

                        short value = rowBuffer[tileX];

                        if (linearizationTable != null)
                          {
                            value = (short)linearizationTable[value & 0xFFFF];
                          }

                        int cfaIndex = (y % 2) * 2 + (x % 2);
                        data[y * scanStride + x * pixelStride + cfaOffsets[cfaIndex]] = value;
                      }
                  }

                tileIndex++;
                ir.processImageProgress((100f * tileIndex) / (tilesDown * tilesAcross));
              }
          }

        iis.setByteOrder(byteOrderSaved);
      }
  }