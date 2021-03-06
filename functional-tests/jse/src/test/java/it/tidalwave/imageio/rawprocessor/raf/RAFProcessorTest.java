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
package it.tidalwave.imageio.rawprocessor.raf;

import javax.annotation.Nonnull;
import java.util.Collection;
import it.tidalwave.imageio.test.ExpectedResults;
import it.tidalwave.imageio.test.NewImageReaderTestSupport;
import it.tidalwave.imageio.raw.RAWImageReadParam;
import it.tidalwave.imageio.raw.Source;
import org.junit.runners.Parameterized.Parameters;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
public class RAFProcessorTest extends NewImageReaderTestSupport
  {
    public RAFProcessorTest (final @Nonnull ExpectedResults expectedResults)
      {
        super(expectedResults);
      }

    @Nonnull
    @Parameters
    public static Collection<Object[]> expectedResults()
      {
        return fixed
          (
            // S6500fd
            ExpectedResults.create("https://imaging.dev.java.net/nonav/TestSets/tmlehto/Fujifilm/S6500fd/RAF/DSCF6315.RAF").
                            image(2848, 2136, 3, 8, "230d59e11a1e459b444d40a77f196244").
                            thumbnail(160, 120).
                            thumbnail(1600, 1200).
                            issues("JRW-127", "JRW-212", "JRW-213"),
            ExpectedResults.create("https://imaging.dev.java.net/nonav/TestSets/tmlehto/Fujifilm/S6500fd/RAF/DSCF6315.RAF").
                            param(new RAWImageReadParam(Source.RAW_IMAGE)).
                            image(3592, 3591, 3, 16, "8c256e68fe9897a4fac12a06f1a07fb4").
                            thumbnail(160, 120).
                            thumbnail(1600, 1200).
                            issues("JRW-204").
                            metadata("metadata.fujiRawData.header", "FUJIFILMCCD-RAW 0201FF389701FinePix S6500fd\u0000\u0000\u0000\u0000\u0000").
//                            metadata("metadata.fujiRawData.version", "0100").
                            metadata("metadata.fujiRawData.version", "\u0000\u0000\u0000\u0000").
                            metadata("metadata.fujiRawData.b1", new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 48, 49, 48, 48}).
                            metadata("metadata.fujiRawData.b2", new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}).
                            metadata("metadata.fujiRawData.JPEGImageOffset", 148).
                            metadata("metadata.fujiRawData.JPEGImageLength", 650086).
                            metadata("metadata.fujiRawData.table1Offset", 650246).
                            metadata("metadata.fujiRawData.table1Length", 4490).
                            metadata("metadata.fujiRawData.CFAOffset", 654736).
                            metadata("metadata.fujiRawData.CFALength", 13043712).
                            metadata("metadata.fujiRawData.unused1", 0).
                            metadata("metadata.fujiRawData.unused2", 13043712).
                            metadata("metadata.fujiRawData.unused3", 0).
                            metadata("metadata.fujiRawData.unused4", 0).
                            metadata("metadata.fujiRawData.unused5", 0).
                            metadata("metadata.fujiRawData.unused6", 0).
                            metadata("metadata.fujiRawData.unused7", 0).
                            metadata("metadata.fujiRawData.unused8", 0).
                            metadata("metadata.fujiRawData.unused9", 0).
                            metadata("metadata.fujiRawData.unused10", 0).
                            metadata("metadata.fujiRawData.fujiTable1.width", 4096).
                            metadata("metadata.fujiRawData.fujiTable1.height", 1544).
                            metadata("metadata.fujiRawData.fujiTable1.rawWidth", 4224).
                            metadata("metadata.fujiRawData.fujiTable1.rawHeight", 1544).
                            metadata("metadata.fujiRawData.fujiTable1.fujiLayout", false).
                            metadata("metadata.fujiRawData.fujiTable1.coefficients", new short[]{336, 518, 336, 489}),
            // S9500
            ExpectedResults.create("https://imaging.dev.java.net/nonav/TestSets/peterbecker/Fujifilm/FinePixS9500/RAF/DSCF3756.RAF").
                            image(2848, 2136, 3, 8, "963d1ed769b35a91e1325f62a3b423c7").
                            thumbnail(160, 120).
                            thumbnail(1600, 1200).
                            issues("JRW-204").
                            issues("JRW-252").
                            metadata("metadata.fujiRawData.header", "FUJIFILMCCD-RAW 0201FF393101FinePix S9500  \u0000\u0000\u0000\u0000\u0000").
//                            metadata("metadata.fujiRawData.version", "0100").
                            metadata("metadata.fujiRawData.version", "\u0000\u0000\u0000\u0000").
                            metadata("metadata.fujiRawData.b1", new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 48, 50, 54, 57}).
                            metadata("metadata.fujiRawData.b2", new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}).
                            metadata("metadata.fujiRawData.JPEGImageOffset", 148).
                            metadata("metadata.fujiRawData.JPEGImageLength", 611759).
                            metadata("metadata.fujiRawData.table1Offset", 611914).
                            metadata("metadata.fujiRawData.table1Length", 3958).
                            metadata("metadata.fujiRawData.CFAOffset", 615872).
                            metadata("metadata.fujiRawData.CFALength", 18528512).
                            metadata("metadata.fujiRawData.unused1", 0).
                            metadata("metadata.fujiRawData.unused2", 18528512).
                            metadata("metadata.fujiRawData.unused3", 0).
                            metadata("metadata.fujiRawData.unused4", 0).
                            metadata("metadata.fujiRawData.unused5", 0).
                            metadata("metadata.fujiRawData.unused6", 0).
                            metadata("metadata.fujiRawData.unused7", 0).
                            metadata("metadata.fujiRawData.unused8", 0).
                            metadata("metadata.fujiRawData.unused9", 0).
                            metadata("metadata.fujiRawData.unused10", 0).
                            metadata("metadata.fujiRawData.fujiTable1.width", 2448).
                            metadata("metadata.fujiRawData.fujiTable1.height", 3688).
                            metadata("metadata.fujiRawData.fujiTable1.rawWidth", 2512).
                            metadata("metadata.fujiRawData.fujiTable1.rawHeight", 3688).
                            metadata("metadata.fujiRawData.fujiTable1.fujiLayout", true).
                            metadata("metadata.fujiRawData.fujiTable1.coefficients", new short[]{320, 502, 320, 422}),
            ExpectedResults.create("https://imaging.dev.java.net/nonav/TestSets/peterbecker/Fujifilm/FinePixS9500/RAF/DSCF3756.RAF").
                            param(new RAWImageReadParam(Source.RAW_IMAGE)).
                            image(4292, 4291, 3, 16, "f53e19fbd1f512ddf052e13097735383").
                            thumbnail(160, 120).
                            thumbnail(1600, 1200).
                            issues("JRW-252").
                            metadata("metadata.fujiRawData.header", "FUJIFILMCCD-RAW 0201FF393101FinePix S9500  \u0000\u0000\u0000\u0000\u0000").
//                            metadata("metadata.fujiRawData.version", "0100").
                            metadata("metadata.fujiRawData.version", "\u0000\u0000\u0000\u0000").
                            metadata("metadata.fujiRawData.b1", new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 48, 50, 54, 57}).
                            metadata("metadata.fujiRawData.b2", new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}).
                            metadata("metadata.fujiRawData.JPEGImageOffset", 148).
                            metadata("metadata.fujiRawData.JPEGImageLength", 611759).
                            metadata("metadata.fujiRawData.table1Offset", 611914).
                            metadata("metadata.fujiRawData.table1Length", 3958).
                            metadata("metadata.fujiRawData.CFAOffset", 615872).
                            metadata("metadata.fujiRawData.CFALength", 18528512).
                            metadata("metadata.fujiRawData.unused1", 0).
                            metadata("metadata.fujiRawData.unused2", 18528512).
                            metadata("metadata.fujiRawData.unused3", 0).
                            metadata("metadata.fujiRawData.unused4", 0).
                            metadata("metadata.fujiRawData.unused5", 0).
                            metadata("metadata.fujiRawData.unused6", 0).
                            metadata("metadata.fujiRawData.unused7", 0).
                            metadata("metadata.fujiRawData.unused8", 0).
                            metadata("metadata.fujiRawData.unused9", 0).
                            metadata("metadata.fujiRawData.unused10", 0).
                            metadata("metadata.fujiRawData.fujiTable1.width", 2448).
                            metadata("metadata.fujiRawData.fujiTable1.height", 3688).
                            metadata("metadata.fujiRawData.fujiTable1.rawWidth", 2512).
                            metadata("metadata.fujiRawData.fujiTable1.rawHeight", 3688).
                            metadata("metadata.fujiRawData.fujiTable1.fujiLayout", true).
                            metadata("metadata.fujiRawData.fujiTable1.coefficients", new short[]{320, 502, 320, 422})
          );
      }
  }

