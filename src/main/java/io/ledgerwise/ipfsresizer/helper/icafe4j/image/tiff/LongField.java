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

package io.ledgerwise.ipfsresizer.helper.icafe4j.image.tiff;

import io.ledgerwise.ipfsresizer.helper.icafe4j.string.StringUtils;

/**
 * TIFF Long type field.
 * 
 * @author Wen Yu, yuwen_66@yahoo.com
 * @version 1.0 01/06/2013
 */
public final class LongField extends AbstractLongField {

	public LongField(short tag, int[] data) {
		super(tag, FieldType.LONG, data);
	}

	public String getDataAsString() {
		return StringUtils.longArrayToString(data, 0, TiffField.MAX_STRING_REPR_LEN, true);
	}
}
