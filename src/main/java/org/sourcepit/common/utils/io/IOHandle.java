/**
 * Copyright (c) 2012 Sourcepit.org contributors and others. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.sourcepit.common.utils.io;

import java.io.Closeable;
import java.io.IOException;

/**
 * @author Bernd Vogt <bernd.vogt@sourcepit.org>
 */
public interface IOHandle<R extends Closeable>
{
   R open() throws IOException;
}