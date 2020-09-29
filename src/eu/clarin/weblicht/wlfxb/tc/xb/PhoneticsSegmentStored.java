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

import eu.clarin.weblicht.wlfxb.tc.api.PhoneticsSegment;
import eu.clarin.weblicht.wlfxb.tc.api.Pronunciation;
import eu.clarin.weblicht.wlfxb.utils.CommonAttributes;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import javax.xml.bind.annotation.*;
import javax.xml.namespace.QName;

/**
 * @author Yana Panchenko
 *
 */
@XmlRootElement(name = PhoneticsSegmentStored.XML_NAME)
@XmlAccessorType(XmlAccessType.NONE)
public class PhoneticsSegmentStored implements PhoneticsSegment {

    public static final String XML_NAME = "phonseg";
    @XmlAttribute(name = CommonAttributes.TOKEN_REFERENCE)
    protected String tokRef;
    @XmlElement(name = PronunciationStored.XML_NAME, required = true)
    protected List<PronunciationStored> prons = new ArrayList<PronunciationStored>();
    @XmlAnyAttribute
    protected LinkedHashMap<QName, String> extraAttributes = new LinkedHashMap<QName, String>();


    @Override
    public Pronunciation[] getPronunciations() {
        return prons.toArray(new Pronunciation[prons.size()]);
    }

    @Override
    public LinkedHashMap<String, String> getExtraAttributes() {
        return PhoneticsSegment.super.retrieveAttributes(extraAttributes);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(tokRef);
        sb.append(" ");
        sb.append(prons.toString());
        return sb.toString();
    }
}
