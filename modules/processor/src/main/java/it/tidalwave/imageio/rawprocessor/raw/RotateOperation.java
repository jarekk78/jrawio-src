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
package it.tidalwave.imageio.rawprocessor.raw;

import it.tidalwave.imageio.raw.Source;
import javax.annotation.Nonnull;
import java.util.Properties;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.Raster;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;
import it.tidalwave.imageio.tiff.IFD;
import it.tidalwave.imageio.tiff.TIFFMetadataSupport;
import it.tidalwave.imageio.rawprocessor.OperationSupport;
import it.tidalwave.imageio.rawprocessor.PipelineArtifact;
import it.tidalwave.imageio.util.Logger;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
public class RotateOperation extends OperationSupport
  {
    private final static Logger logger = getLogger(RotateOperation.class);

    private int rotation;

    /*******************************************************************************************************************
     *
     *
     ******************************************************************************************************************/
    public RotateOperation()
      {
        super(Source.Type.RAW, Source.Type.NORMAL);
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     * It's important to compute rotation here since it can be needed by other operations' init().
     *
     ******************************************************************************************************************/
    @Override
    public void init (final @Nonnull PipelineArtifact artifact)
      {
        logger.fine("init(%s)", artifact);

        rotation = getCameraOrientation(artifact);

        if (rotation != 0)
          {
            artifact.setRotation(rotation);
          }
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    public void process (final @Nonnull PipelineArtifact artifact)
      {
        logger.fine("process(%s)", artifact);
        
        if (rotation != 0)
          {
            artifact.setImage(rotateQuadrant(artifact.getImage(), rotation));
          }
      }
        
    /*******************************************************************************************************************
     *
     * Reads the camera embedded orientation. This method works with EXIF data:
     * RAW processors for other formats should override this method. 
     *
     ******************************************************************************************************************/
    protected int getCameraOrientation (final @Nonnull PipelineArtifact artifact)
      {
        final TIFFMetadataSupport metadata = (TIFFMetadataSupport)artifact.getRAWMetadata();
        final IFD primaryIFD = metadata.getPrimaryIFD();
        final IFD exifIFD = metadata.getExifIFD();
        int orientation = 0;
        IFD.Orientation tiffOrientation = null;

        if (primaryIFD.isOrientationAvailable())
          {
            tiffOrientation = primaryIFD.getOrientation();
          }
          
        else if (exifIFD.isOrientationAvailable())
          {
            tiffOrientation = exifIFD.getOrientation();
          }
                    
        if (tiffOrientation == IFD.Orientation.LEFT_BOTTOM)
          {
            orientation = -90;
          }

        if (tiffOrientation == IFD.Orientation.RIGHT_TOP)
          {
            orientation = 90;
          }

        logger.finer(">>>> camera orientation: %s, degrees: %d", tiffOrientation, orientation);

        return orientation;
      }

    /*******************************************************************************************************************
     *
     *
     ******************************************************************************************************************/
    @Nonnull
    protected static BufferedImage rotateQuadrant (final @Nonnull BufferedImage image, int degrees)
      {
        logger.finer("rotateQuadrant(%d)", degrees);
        logImage(logger, ">>>> image: ", image);
        SampleModel sampleModel = image.getSampleModel();
        
        if (degrees < 0)
          {
            degrees += 360;  
          }

        if ((degrees == 90) || (degrees == 270))
          {
            sampleModel = sampleModel.createCompatibleSampleModel(image.getHeight(), image.getWidth());
          }

        final WritableRaster newRaster = Raster.createWritableRaster(sampleModel, null);
        final ColorModel colorModel = image.getColorModel();
        final BufferedImage result = new BufferedImage(colorModel, newRaster, false, getProperties(image));

        final Graphics2D g2d = (Graphics2D)result.getGraphics();

        try
          {
            final double radians = Math.toRadians(degrees);
            g2d.transform(AffineTransform.getRotateInstance(radians));

            int x = 0;
            int y = 0;

            switch (degrees)
              {
                case 90:
                  y = -image.getHeight();

                  break;

                case 180:
                  x = -image.getWidth();
                  y = -image.getHeight();

                  break;

                case 270:
                  x = -image.getWidth();

                  break;
              }

            g2d.drawImage(image, x, y, null);
          }

        finally
          {
            g2d.dispose();
          }

        logImage(logger, ">>>> rotateQuadrant() returning ", result);
        
        return result;
      }
    
    /*******************************************************************************************************************
     *
     *
     ******************************************************************************************************************/
    @Nonnull
    private static Properties getProperties (final @Nonnull BufferedImage image)
      {
        final Properties properties = new Properties();
        final String[] propertyNames = image.getPropertyNames();

        if (propertyNames != null)
          {
            for (int i = 0; i < propertyNames.length; i++)
              {
                String propertyName = propertyNames[i];
                Object propertyValue = image.getProperty(propertyName);
                properties.setProperty(propertyName, propertyValue.toString());
              }
          }

        return properties;
      }  
  }
