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
package it.tidalwave.imageio.rawprocessor.crw;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.util.Arrays;
import java.awt.image.WritableRaster;
import it.tidalwave.imageio.rawprocessor.PipelineArtifact;
import it.tidalwave.imageio.rawprocessor.raw.CurveOperation;
import it.tidalwave.imageio.crw.CRWMetadata;
import it.tidalwave.imageio.util.Logger;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
public class CRWCurveOperation extends CurveOperation  
  {
    private final static String CLASS = CRWCurveOperation.class.getName();
    private final static Logger logger = Logger.getLogger(CLASS);

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override
    @Nonnull
    protected int[] getBlackLevel (@Nonnull final PipelineArtifact artifact)
      {
        logger.fine("getBlackLevel(%s)", artifact);

        final WritableRaster raster = artifact.getImage().getRaster();
        final CRWMetadata metadata = (CRWMetadata)artifact.getRAWMetadata();

        // TODO: document why use these margins
        final int yMin = metadata.getSensorTopBorder() /*- 5*/;
        final int yMax = metadata.getSensorBottomBorder() /*+ 5*/;
        final int xMin = 2;
        final int xMax = metadata.getSensorLeftBorder() - 13;
        logger.finer(">>>> computing black level from (%d; %d) - (%d; %d)...", xMin, xMax, yMin, yMax);

        final double[] dark = new double[2];

        for (int y = yMin; y <= yMax; y++)
          {
            for (int x = xMin; x <= xMax; x++)
              {
                final int s1 = raster.getSample(x, y, 0);
                final int s2 = raster.getSample(x, y, 1);
                final int s3 = raster.getSample(x, y, 2);
                dark[x & 1] += s1 + s2 + s3;
//                  System.err.printf("%d %d %d\n", s1, s2, s3);
              }
          }

        logger.finer(">>>> dark: %s", Arrays.toString(dark));
        final int sampleCount = (yMax - yMin + 1) * (xMax - xMin + 1) / 2; // each row adds a single sample, so divide by 2
        final int black = (int)((dark[0] + dark[1]) / (2 * sampleCount));

        return new int[]{ black, black, black };
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Nonnegative
    @Override
    protected double getWhiteLevel (final @Nonnull PipelineArtifact artifact)
      {
        logger.fine("getWhiteLevel(%s)", artifact);
        return (1 << 12) - 1; // FIXME
      }
  }
