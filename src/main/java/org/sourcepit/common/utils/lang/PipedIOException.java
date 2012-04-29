/**
 * Copyright (c) 2012 Sourcepit.org contributors and others. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.sourcepit.common.utils.lang;

import java.io.IOException;

/**
 * @author Bernd Vogt <bernd.vogt@sourcepit.org>
 */
public class PipedIOException extends PipedException
{
   private static final long serialVersionUID = 1L;

   PipedIOException(IOException cause)
   {
      super(cause);
   }

   PipedIOException(ThrowablePipe cause)
   {
      super(cause);
   }
   
   @Override
   public IOException getCause()
   {
      return (IOException) super.getCause();
   }

}
