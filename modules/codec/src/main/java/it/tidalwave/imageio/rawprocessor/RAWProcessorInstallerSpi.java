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
package it.tidalwave.imageio.rawprocessor;

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import javax.imageio.spi.ImageInputStreamSpi;
import javax.imageio.stream.ImageInputStream;

/**
 *
 * @author Fabrizio Giudici
 *
 * This is a dummy Spi used only for installing the RAWProcessor stuff. It's a temporary
 * trick to keep the RAWProcessor stuff in a different sub-project until it gets mature.
 *
 */
public class RAWProcessorInstallerSpi extends ImageInputStreamSpi
  {
    public RAWProcessorInstallerSpi()
      {
        super("dummy", "dummy", Float.class);
        install();
      }

    public ImageInputStream createInputStreamInstance (Object object, boolean b, File file) throws IOException
      {
        return null;
      }

    public String getDescription (Locale locale)
      {
        return "dummy";
      }
    
    private void install()
      {
        try
          {
            Class.forName("it.tidalwave.imageio.rawprocessor.RAWProcessorInstaller");
            System.err.println("RAWProcessor succesfully installed");
          }        
        catch (ClassNotFoundException e)
          {
            System.err.println("RAWProcessor not found: " + e);
          }        
        catch (Throwable e)
          {
            System.err.println("Error while running RAWProcessorInstaller: " + e.toString());
          }        
      }
  }
