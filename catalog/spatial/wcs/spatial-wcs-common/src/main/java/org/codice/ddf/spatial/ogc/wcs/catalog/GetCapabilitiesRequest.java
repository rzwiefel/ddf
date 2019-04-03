/**
 * Copyright (c) Connexta, LLC
 *
 * <p>This is free software: you can redistribute it and/or modify it under the terms of the GNU
 * Lesser General Public License as published by the Free Software Foundation, either version 3 of
 * the License, or any later version.
 *
 * <p>This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details. A copy of the GNU Lesser General Public
 * License is distributed along with this program and can be found at
 * <http://www.gnu.org/licenses/lgpl.html>.
 */
package org.codice.ddf.spatial.ogc.wcs.catalog;

/**
 * JAX-RS Parameter Bean Class for the GetCapabilities request. The member variables will be
 * automatically injected by the JAX-RS annotations.
 */
public class GetCapabilitiesRequest {

  private String service = WcsConstants.WCS;

  // The following parameters are optional for the GetCapabilities Request
  private String acceptVersions = WcsConstants.VERSION_1_0_0;

  private String updateSequence;
}
