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
package it.tidalwave.imageio.makernote;

import javax.annotation.Nonnull;
import java.io.IOException;
import it.tidalwave.imageio.io.RAWImageInputStream;
import it.tidalwave.imageio.util.Logger;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
public class LeicaMakerNote extends LeicaMakerNoteSupport
  {
    private final static String CLASS = LeicaMakerNote.class.getName();
    private final static Logger logger = Logger.getLogger(CLASS);
    private final static long serialVersionUID = 435098698732458972L;

    @Override
    public void loadAll (final @Nonnull RAWImageInputStream iis, final long offset)
      throws IOException
      {
        logger.fine("loadAll(%s, %d)", iis, offset);
        final byte[] buffer = new byte[8];
        iis.seek(offset);
        iis.readFully(buffer);
//        logger.finer(">>>> header: ", new String(buffer, 0)); // TODO: should be LEICA and zeros
        super.loadAll(iis, offset + buffer.length);
      }
  }
