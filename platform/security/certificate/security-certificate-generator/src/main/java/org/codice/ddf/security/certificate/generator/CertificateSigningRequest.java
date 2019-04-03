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
package org.codice.ddf.security.certificate.generator;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.cert.X509Certificate;
import java.util.LinkedHashSet;
import java.util.Set;
import org.apache.commons.lang.Validate;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.GeneralNames;
import org.bouncycastle.cert.CertIOException;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.joda.time.DateTime;

/**
 * Model of a X509 certificate signing request. These values must be set:
 *
 * <p>
 *
 * <ul>
 *   <li>notAfter - certificate expiration date
 *   <li>subjectName - typically a server's FQDN.
 *   <li>certificateAuthority - instance of {@link CertificateAuthority} who will signed this
 *       request
 * </ul>
 *
 * <p>These values may be optionally set:
 *
 * <p>
 *
 * <ul>
 *   <li>serialNumber - arbitrary serial number
 * </ul>
 *
 * @see <a href="https://www.ietf.org/rfc/rfc4514.txt">RFC 5280, Internet X.509 Public Key
 *     Infrastructure Certificate</a>
 */
public class CertificateSigningRequest {

  public static final int VALID_YEARS = 100;

  static {
    Security.addProvider(new BouncyCastleProvider());
  }

  protected DateTime notBefore;

  protected DateTime notAfter;

  protected KeyPair subjectKeyPair;

  protected BigInteger serialNumber;

  private X500Name subjectName;

  private Set<GeneralName> subjectAltNames = new LinkedHashSet<>(8); // to preserve order

  public CertificateSigningRequest() {
    initialize();
  }

  public PrivateKey getSubjectPrivateKey() {
    return subjectKeyPair.getPrivate();
  }

  public PublicKey getSubjectPublicKey() {
    return subjectKeyPair.getPublic();
  }

  /**
   * Create a distinguished name for the certificate's subject. Currently, only the common name
   * (sub-attribute of the distinguished name) is supported. The common name should be the <b>fully
   * qualified domain name</b> of the certificate's subject (i.e. the server). For example,
   * <i>server.subdomain.domain.tld</i>.
   *
   * @param name subject's common name attribute (
   */
  public void setCommonName(String name) {
    Validate.notNull(name, "Subject common name of certificate signing request cannot be null");
    subjectName = PkiTools.makeDistinguishedName(name);
  }

  public void setDistinguishedName(String... name) {
    Validate.notNull(name, "Subject DN of certificate signing request cannot be null");
    subjectName = PkiTools.convertDistinguishedName(name);
  }

  /**
   * Set the serial number of the certificate. The serial number is arbitrary, but should not be
   * negative.
   *
   * @param number arbitrary serial number
   */
  public void setSerialNumber(long number) {
    Validate.isTrue(number > 0, "Serial number for X509 certificate should not be negative");
    serialNumber = BigInteger.valueOf(number);
  }

  /**
   * Set the subject's public and private keypair. Only the public key is used. However, the the
   * most common use case is to generate a new keypair when creating a certificate. The client code
   * will need to retrieve the the private key for later use. This method is provided for situations
   * when an existing keypair should be used to generate the certificate.
   *
   * @param keyPair RSA public/private keys
   */
  public void setSubjectKeyPair(KeyPair keyPair) {
    Validate.notNull(keyPair, "Subject public/private keypair cannot be null");
    subjectKeyPair = keyPair;
  }

  JcaX509v3CertificateBuilder newCertificateBuilder(X509Certificate certificate)
      throws CertIOException {
    final JcaX509v3CertificateBuilder cbuilder =
        new JcaX509v3CertificateBuilder(
            certificate,
            serialNumber,
            notBefore.toDate(),
            notAfter.toDate(),
            subjectName,
            getSubjectPublicKey());

    if (!subjectAltNames.isEmpty()) {
      cbuilder.addExtension(
          Extension.subjectAlternativeName,
          false,
          new GeneralNames(subjectAltNames.toArray(new GeneralName[subjectAltNames.size()])));
    }
    return cbuilder;
  }

  // Set reasonable defaults
  private void initialize() {
    setSerialNumber(System.currentTimeMillis());
    setNotBefore(DateTime.now().minusDays(1));
    setNotAfter(getNotBefore().plusYears(VALID_YEARS));
    setCommonName(PkiTools.getHostName());
    setSubjectKeyPair(PkiTools.generateRsaKeyPair());
  }

  public DateTime getNotBefore() {
    return notBefore;
  }

  /**
   * The validity period for a certificate is the period of time from notBefore through notAfter,
   * inclusive.
   *
   * @param date expiration date of the certificate
   */
  public void setNotBefore(DateTime date) {
    Validate.notNull(date, "Certificate 'not before' date cannot be null");
    Validate.isTrue(
        date.isBefore(notAfter), "Certificate 'not after' date must come after 'not before' date");
    notBefore = date;
  }

  public DateTime getNotAfter() {
    return notAfter;
  }

  /**
   * The validity period for a certificate is the period of time from notBefore through notAfter,
   * inclusive.
   *
   * @param date effective date
   */
  public void setNotAfter(DateTime date) {
    Validate.notNull(date, "Certificate 'not after' date cannot be null");
    Validate.isTrue(
        date.isAfter(notBefore), "Certificate 'not after' date must come after 'not before' date");
    notAfter = date;
  }

  public X500Name getSubjectName() {
    return subjectName;
  }

  public void setSubjectName(X500Name subjectName) {
    this.subjectName = subjectName;
  }

  public long getSerialNumber() {
    return serialNumber.longValue();
  }

  public void setSerialNumber(BigInteger serialNumber) {
    this.serialNumber = serialNumber;
  }

  /**
   * Adds subject alternative names. The operation is idempotent, so duplicate SANs will not appear
   * on the CSR. Note however that the order that SANs get added do influence the final result.
   *
   * @param names a set of alternative names to add in a format similar to OpenSSL X509
   *     configuration
   * @throws IllegalArgumentException if the list or any elements are <code>null</code> or again if
   *     any elements failed to be properly parsed
   */
  public void addSubjectAlternativeNames(String... names) {
    Validate.notNull(
        names, "Subject alternative names of certificate signing request cannot be null");
    Validate.noNullElements(
        names, "Subject alternative names of certificate signing request cannot be null");
    for (final String n : names) {
      subjectAltNames.add(PkiTools.makeGeneralName(n));
    }
  }

  /**
   * Gets the subject alternative names.
   *
   * @return a set of all subject alternative names in the order they were added
   */
  public Set<GeneralName> getSubjectAlternativeNames() {
    return subjectAltNames;
  }
}
