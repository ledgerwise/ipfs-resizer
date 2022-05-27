/**
 * COPYRIGHT (C) 2014-2019 WEN YU (YUWEN_66@YAHOO.COM) ALL RIGHTS RESERVED.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Any modifications to this file must keep this entire header intact.
 */

package io.ledgerwise.ipfsresizer.helper.icafe4j.image.meta.iptc;

public interface IPTCTag {
	public int getRecordNumber();

	public int getTag();

	public String getName();

	public boolean allowMultiple();

	public String getDataAsString(byte[] data);

	public static final int MAX_STRING_REPR_LEN = 10;
}
