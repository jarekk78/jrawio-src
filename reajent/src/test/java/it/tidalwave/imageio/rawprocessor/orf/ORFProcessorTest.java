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
 * $Id$
 *
 **********************************************************************************************************************/
package it.tidalwave.imageio.rawprocessor.orf;

import javax.annotation.Nonnull;
import java.util.Collection;
import javax.imageio.ImageReader;
import it.tidalwave.imageio.ExpectedResults;
import it.tidalwave.imageio.NewImageReaderTestSupport;
import it.tidalwave.imageio.orf.CameraSettings;
import it.tidalwave.imageio.orf.Equipment;
import it.tidalwave.imageio.orf.FocusInfo;
import it.tidalwave.imageio.orf.ImageProcessing;
import it.tidalwave.imageio.orf.ORFMetadata;
import it.tidalwave.imageio.orf.OlympusMakerNote;
import it.tidalwave.imageio.orf.RawDevelopment;
import org.junit.runners.Parameterized.Parameters;
import static org.junit.Assert.*;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
public class ORFProcessorTest extends NewImageReaderTestSupport
  {
    public ORFProcessorTest (final @Nonnull ExpectedResults expectedResults)
      {
        super(expectedResults);
      }

    @Nonnull
    @Parameters
    public static Collection<Object[]> expectedResults()
      {
        return fixed
          (
            // Olympus C5050Z
            ExpectedResults.create("http://www.rawsamples.ch/raws/olympus/c5050z/RAW_OLYMPUS_C5050Z.ORF").
                            image(2560, 1920, 3, 8, "66674b6ff662dee2f83daa53dad5009a").
                            issues("JRW-231", "JRW-232", "JRW-236", "JRW-249"),
            // Olympus C8080
            ExpectedResults.create("http://www.rawsamples.ch/raws/olympus/c8080/RAW_OLYMPUS_C8080.ORF").
                            image(3264, 2448, 3, 8, "d0fa32b5ed680f0831f867143f678c6e").
                            issues("JRW-231", "JRW-232", "JRW-249"),
            // Olympus E1
            ExpectedResults.create("http://www.rawsamples.ch/raws/olympus/e1/RAW_OLYMPUS_E1.ORF").
                            image(2560, 1920, 3, 8, "36aa7c13f34a00281ab341789cc12631").
                            thumbnail(160, 120).
                            thumbnail(1280, 960).
                            issues("JRW-231", "JRW-249"),
            // Olympus E20
            ExpectedResults.create("http://www.rawsamples.ch/raws/olympus/e20/RAW_OLYMPUS_E20.ORF").
                            image(2572, 1920, 3, 8, "e25d9ceb9cad29cd3ffbee52a28e7e6b").
                            thumbnail(160, 120).
                            issues("JRW-231", "JRW-249"),
            // Olympus E3
            ExpectedResults.create("http://www.rawsamples.ch/raws/olympus/e3/RAW_OLYMPUS_E3.ORF").
                            image(3648, 2736, 3, 8, "d7762fa9a4548bec5c250cb5c827d981").
                            thumbnail(1600, 1200).
                            issues("JRW-231", "JRW-236", "JRW-249"),
            // Olympus E300
            ExpectedResults.create("http://www.rawsamples.ch/raws/olympus/e300/RAW_OLYMPUS_E300.ORF").
                            image(3264, 2448, 3, 8, "309f9ddffa587b14c4917651ef2a8134").
                            thumbnail(160, 120).
                            thumbnail(1600, 1200).
                            issues("JRW-231", "JRW-249"),
            // Olympus E410
            ExpectedResults.create("http://www.rawsamples.ch/raws/olympus/e410/RAW_OLYMPUS_E410.ORF").
                            image(3648, 2736, 3, 8, "741ee76169d4ac572bc8be70ab2ebbf5").
                            thumbnail(1600, 1200).
                            issues("JRW-231", "JRW-249"),
            // Olympus E500
            ExpectedResults.create("http://www.rawsamples.ch/raws/olympus/e500/RAW_OLYMPUS_E500.ORF").
                            image(3264, 2448, 3, 8, "c617af4d2d20b73aac779344232f8f5a").
                            thumbnail(160, 120).
                            thumbnail(1600, 1200).
                            issues("JRW-231"),
            // Olympus SP350
            ExpectedResults.create("http://www.rawsamples.ch/raws/olympus/sp350/RAW_OLYMPUS_SP350.ORF").
                            image(3264, 2448, 3, 8, "d954d7c4215328e11e2520c77fc14e1b").
                            issues("JRW-231", "JRW-232", "JRW-236", "JRW-249"),
            // Olympus SP500UZ
            ExpectedResults.create("http://www.rawsamples.ch/raws/olympus/sp500uz/RAW_OLYMPUS_SP500UZ.ORF").
                            image(2816, 2112, 3, 8, "80304b6a62646377a7621f6fa8e36d39").
                            issues("JRW-231", "JRW-232", "JRW-236", "JRW-249"),
            // Olympus E500
            ExpectedResults.create("https://imaging.dev.java.net/nonav/TestSets/others/victoriagracia/Olympus/E500/ORF/V7020205.ORF").
                            image(3264, 2448, 3, 8, "0020be22c4224b51d60807502ff8c8af").
                            thumbnail(160, 120).
                            thumbnail(1600, 1200).
                            issues("JRW-160", "JRW-211", "JRW-214", "JRW-249").
                            metadata("metadata.olympusMakerNote.imageWidth", 3264).
                            metadata("metadata.olympusMakerNote.imageHeight", 2448).
                            extra(new ExpectedResults.Extra()
                              {
                                public void run (final @Nonnull ImageReader ir)
                                  throws Exception
                                  {
                                    final ORFMetadata metadata = (ORFMetadata)ir.getImageMetadata(0);
                                    assertNotNull(metadata);
                                    final OlympusMakerNote makerNote = metadata.getOlympusMakerNote();
                                    assertNotNull(makerNote);
                                    assertEquals(27, makerNote.getTags().size());

                                    final CameraSettings cameraSettings = makerNote.getOlympusCameraSettings();
                                    assertNotNull(cameraSettings);
                                    assertEquals(40, cameraSettings.getTags().size());

                                    final Equipment equipment = makerNote.getOlympusEquipment();
                                    assertNotNull(equipment);
                                    assertEquals(23, equipment.getTags().size());

                                    final FocusInfo focusInfo = makerNote.getOlympusFocusInfo();
                                    assertNotNull(focusInfo);
                                    assertEquals(52, focusInfo.getTags().size());

                                    final ImageProcessing imageProcessing = makerNote.getOlympusImageProcessing();
                                    assertNotNull(imageProcessing);
                                    assertEquals(109, imageProcessing.getTags().size());

                                    final RawDevelopment rawDevelopment = makerNote.getOlympusRawDevelopment();
                                    assertNotNull(rawDevelopment);
                                    assertEquals(14, rawDevelopment.getTags().size());
                                  }
                              }),
            // Olympus E510
            ExpectedResults.create("https://imaging.dev.java.net/nonav/TestSets/others/josephandre/Olympus/E510/ORF/_2090037.ORF").
                            image(3648, 2736, 3, 8, "4e92f94313383cfaa52f7415ac3c5ad3").
                            thumbnail(1600, 1200).
                            issues("JRW-151", "JRW-154", "JRW-155", "JRW-159", "JRW-211", "JRW-214", "JRW-249").
                            metadata("metadata.olympusMakerNote.olympusImageProcessing.imageWidth", 3648).
                            metadata("metadata.olympusMakerNote.olympusImageProcessing.imageHeight", 2736).
                            extra(new ExpectedResults.Extra()
                              {
                                public void run (final @Nonnull ImageReader ir)
                                  throws Exception
                                  {
                                    final ORFMetadata metadata = (ORFMetadata)ir.getImageMetadata(0);
                                    assertNotNull(metadata);
                                    final OlympusMakerNote makerNote = metadata.getOlympusMakerNote();
                                    assertNotNull(makerNote);
                                    assertEquals(8, makerNote.getTags().size());

                                    final CameraSettings cameraSettings = makerNote.getOlympusCameraSettings();
                                    assertNotNull(cameraSettings);
                                    assertEquals(44, cameraSettings.getTags().size());

                                    final Equipment equipment = makerNote.getOlympusEquipment();
                                    assertNotNull(equipment);
                                    assertEquals(23, equipment.getTags().size());

                                    final FocusInfo focusInfo = makerNote.getOlympusFocusInfo();
                                    assertNotNull(focusInfo);
                                    assertEquals(59, focusInfo.getTags().size());

                                    final ImageProcessing imageProcessing = makerNote.getOlympusImageProcessing();
                                    assertNotNull(imageProcessing);
                                    assertEquals(142, imageProcessing.getTags().size());

                                    final RawDevelopment rawDevelopment = makerNote.getOlympusRawDevelopment();
                                    assertNotNull(rawDevelopment);
                                    assertEquals(14, rawDevelopment.getTags().size());
                                  }
                              })
          );
      }
  }
