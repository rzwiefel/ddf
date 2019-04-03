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
package ddf.security.sts;

import ddf.security.PropertiesLoader;
import java.util.Properties;

/** A wrapper to handle properties with variables */
public class PropertiesWrapper extends Properties {
  public PropertiesWrapper(String propsFile) {
    super();
    this.putAll(PropertiesLoader.loadProperties(propsFile));
  }
}
