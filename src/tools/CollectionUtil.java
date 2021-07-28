/*
 This file is part of the OdinMS Maple Story Server
 Copyright (C) 2008 ~ 2010 Patrick Huy <patrick.huy@frz.cc> 
 Matthias Butz <matze@odinms.de>
 Jan Christian Meyer <vimes@odinms.de>

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU Affero General Public License version 3
 as published by the Free Software Foundation. You may not use, modify
 or distribute this program under any other version of the
 GNU Affero General Public License.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU Affero General Public License for more details.

 You should have received a copy of the GNU Affero General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package tools;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides utilities for manipulating collections of objects.
 *
 * @author Frz
 * @version 1.0
 * @since Revision 701
 */
public class CollectionUtil {

    /**
     * Static class dummy constructor
     */
    private CollectionUtil() {
        // mhwaha
        // -Insert evil laugh here-
    }

    /**
     * Copies <code>count</code> items off of list, starting from the beginning.
     *
     * @param <T> The type of the list.
     * @param list The list to copy from.
     * @param count The number of items to copy.
     * @return The copied list.
     */
    public static <T> List<T> copyFirst(List<T> list, int count) {
        List<T> ret = new ArrayList<T>(list.size() < count ? list.size() : count);
        int i = 0;
        for (T elem : list) {
            ret.add(elem);
            if (i++ > count) {
                break;
            }
        }
        return ret;
    }
}
