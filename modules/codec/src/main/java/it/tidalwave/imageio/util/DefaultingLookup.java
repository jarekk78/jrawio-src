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
package it.tidalwave.imageio.util;

import javax.annotation.Nonnull;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
public class DefaultingLookup extends DefaultLookup
  {
    private final static String CLASS = DefaultingLookup.class.getName();
    private final static Logger logger = Logger.getLogger(CLASS);
    private final static long serialVersionUID = 4574563455473452L;

    @Nonnull
    private final Lookup delegate;

    /*******************************************************************************************************************
     *
     *
     ******************************************************************************************************************/
    public DefaultingLookup (final @Nonnull Lookup delegate)
      {
        this.delegate = delegate;
      }

    /*******************************************************************************************************************
     *
     *
     ******************************************************************************************************************/
    @Override
    @Nonnull
    public <T> T lookup (final @Nonnull Class<T> type)
      throws NotFoundException
      {
        T defaultValue = null;

        try
          {
            final Method getDefault = type.getMethod("getDefault");
            defaultValue = (T)getDefault.invoke(null);
          }
        catch (InvocationTargetException ex)
          {
          }
        catch (IllegalAccessException ex)
          {
          }
        catch (NoSuchMethodException ex)
          {
          }
        catch (SecurityException ex)
          {
          }
        
        final T result = delegate.lookup(type, defaultValue);
        logger.finest("lookup(%s) returning %s", type, result);
        
        return result;
      }

    /*******************************************************************************************************************
     *
     *
     ******************************************************************************************************************/
    @Override
    public String toContentString()
      {
        return delegate.toContentString();
      }

    /*******************************************************************************************************************
     *
     *
     ******************************************************************************************************************/
    @Override
    @Nonnull
    public String toString()
      {
        return String.format("DefaultingLookup[%s]", delegate);
      }
  }
