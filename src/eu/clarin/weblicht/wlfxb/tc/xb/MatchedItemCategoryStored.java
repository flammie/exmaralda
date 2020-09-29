/**
 * wlfxb - a library for creating and processing of TCF data streams.
 *
 * Copyright (C) University of Tübingen.
 *
 * This file is part of wlfxb.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
/**
 *
 */
package eu.clarin.weblicht.wlfxb.tc.xb;

import eu.clarin.weblicht.wlfxb.tc.api.MatchedItemCategory;
import eu.clarin.weblicht.wlfxb.utils.CommonAttributes;
import java.util.LinkedHashMap;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyAttribute;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.namespace.QName;

/**
 * @author Yana Panchenko
 *
 */
@XmlRootElement(name = MatchedItemCategoryStored.XML_NAME)
@XmlAccessorType(XmlAccessType.NONE)
public class MatchedItemCategoryStored implements MatchedItemCategory {

    public static final String XML_NAME = "category";
    @XmlAttribute(name = CommonAttributes.NAME, required = true)
    protected String name;
    @XmlAttribute(name = CommonAttributes.VALUE, required = true)
    protected String value;
    @XmlAnyAttribute
    protected LinkedHashMap<QName, String> extraAttributes = new LinkedHashMap<QName, String>();

    MatchedItemCategoryStored() {
    }

    MatchedItemCategoryStored(String name, String value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public LinkedHashMap<String, String> getExtraAttributes() {
         return MatchedItemCategory.super.retrieveAttributes(extraAttributes);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(XML_NAME);
        sb.append(" ");
        sb.append(name);
        sb.append(" ");
        sb.append(value);
        return sb.toString();
    }
}
