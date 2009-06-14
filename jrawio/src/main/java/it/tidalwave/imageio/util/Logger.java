/*******************************************************************************
 *
 * jrawio - a Java(TM) ImageIO API Spi Provider for RAW files
 * ==========================================================
 *
 * Copyright (C) 2003-2008 by Fabrizio Giudici
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
 * $Id: IFD.java 140 2008-09-07 12:48:37Z fabriziogiudici $
 *
 ******************************************************************************/
package it.tidalwave.imageio.util;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Handler;
import java.util.logging.Level;

/*******************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id: SoundPlayerImpl.java 5283 2008-04-24 19:26:14Z fabriziogiudici $
 *
 ******************************************************************************/
public class Logger 
  {
    @Nonnull
    private final java.util.logging.Logger logger;

    private final static Map<String, Logger> loggerMapByName = new HashMap<String, Logger>();

    Logger (@Nonnull final java.util.logging.Logger logger)
      {
        this.logger = logger;
      }

    @Nonnull
    public static synchronized Logger getLogger (@Nonnull final String name)
      {
        Logger logger = loggerMapByName.get(name);

        if (logger == null)
          {
            logger = new Logger(java.util.logging.Logger.getLogger(name));
            loggerMapByName.put(name, logger);
          }

        return logger;
      }

    public void warning (@Nonnull final String string, @Nonnull final Object ... args) 
      {
        if (logger.isLoggable(Level.WARNING))
          {
            logger.warning(String.format(string, args));
          }
      }

    public void throwing (String arg0, String arg1, Throwable arg2)
      {
        logger.throwing(arg0, arg1, arg2);
      }

    public void severe (@Nonnull final String string, @Nonnull final Object ... args)
      {
        if (logger.isLoggable(Level.SEVERE))
          {
            logger.severe(String.format(string, args));
          }
      }

    public void info (@Nonnull final String string, @Nonnull final Object ... args) 
      {
        if (logger.isLoggable(Level.INFO))
          {
            logger.info(String.format(string, args));
          }
      }

    public void finest (@Nonnull final String string, @Nonnull final Object ... args) 
      {
        if (logger.isLoggable(Level.FINEST))
          {
            logger.finest(String.format(string, args));
          }
      }
    
    public void finer (@Nonnull final String string, @Nonnull final Object ... args)
      {
        if (logger.isLoggable(Level.FINER))
          {
            logger.finer(String.format(string, args));
          }
      }

    public void fine (@Nonnull final String string, @Nonnull final Object ... args) 
      {
        if (logger.isLoggable(Level.FINE))
          {
            logger.fine(String.format(string, args));
          }
      }
    
    public void setLevel (Level arg0)
      throws SecurityException
      {
        logger.setLevel(arg0);
      }

    public boolean isLoggable(Level arg0) 
      {
        return logger.isLoggable(arg0);
      }

    public String getName() 
      {
        return logger.getName();
      }

    public Level getLevel() 
      {
        return logger.getLevel();
      }

    public synchronized Handler[] getHandlers() 
      {
        return logger.getHandlers();
      }
  }