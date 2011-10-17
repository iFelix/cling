/*
 * Copyright (C) 2011 4th Line GmbH, Switzerland
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 2 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fourthline.cling.support.model.dlna;

import java.util.EnumSet;

/**
 * @author Mario Franco
 */
public class DLNAOperationsAttribute extends DLNAAttribute<EnumSet<DLNAOperations>> {

    public DLNAOperationsAttribute() {
        setValue(EnumSet.of(DLNAOperations.NONE));
    }

    public DLNAOperationsAttribute(DLNAOperations... op) {
        if (op != null && op.length > 0) {
            DLNAOperations first = op[0];
            if (op.length > 1) {
                System.arraycopy(op, 1, op, 0, op.length - 1);
                setValue(EnumSet.of(first, op));
            } else {
                setValue(EnumSet.of(first));
            }
        }
    }

    public void setString(String s, String cf) throws InvalidDLNAProtocolAttributeException {
        EnumSet<DLNAOperations> value = EnumSet.noneOf(DLNAOperations.class);
        try {
            int parseInt = Integer.parseInt(s, 16);
            for (DLNAOperations op : DLNAOperations.values()) {
                int code = op.getCode() & parseInt;
                if (op != DLNAOperations.NONE && (op.getCode() == code)) {
                    value.add(op);
                }
            }
        } catch (NumberFormatException numberFormatException) {
        }

        if (value.isEmpty())
            throw new InvalidDLNAProtocolAttributeException("Can't parse DLNA operations integer from: " + s);

        setValue(value);
    }

    public String getString() {
        int code = DLNAOperations.NONE.getCode();
        for (DLNAOperations op : getValue()) {
            code |= op.getCode();
        }
        return String.format("%02x", code);
    }
}