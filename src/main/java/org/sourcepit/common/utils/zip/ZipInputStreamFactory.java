/**
 * Copyright (c) 2011 Sourcepit.org contributors and others. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.sourcepit.common.utils.zip;

import java.io.IOException;
import java.util.zip.ZipInputStream;

public interface ZipInputStreamFactory
{
   ZipInputStream newZipInputStream() throws IOException;
}