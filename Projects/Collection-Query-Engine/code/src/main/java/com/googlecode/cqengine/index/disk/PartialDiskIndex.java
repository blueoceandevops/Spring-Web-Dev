/**
 * Copyright 2012-2015 Niall Gallagher
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.googlecode.cqengine.index.disk;

import com.googlecode.cqengine.attribute.Attribute;
import com.googlecode.cqengine.index.Index;
import com.googlecode.cqengine.index.support.PartialIndex;
import com.googlecode.cqengine.index.support.PartialSortedKeyStatisticsAttributeIndex;
import com.googlecode.cqengine.index.support.SortedKeyStatisticsAttributeIndex;
import com.googlecode.cqengine.index.support.indextype.DiskTypeIndex;
import com.googlecode.cqengine.persistence.disk.DiskPersistence;
import com.googlecode.cqengine.query.Query;

import static com.googlecode.cqengine.index.sqlite.support.DBUtils.sanitizeForTableName;

/**
 * A {@link PartialIndex} which uses {@link DiskPersistence}.
 *
 * @author niall.gallagher
 */
public class PartialDiskIndex<A extends Comparable<A>, O> extends PartialSortedKeyStatisticsAttributeIndex<A, O> implements DiskTypeIndex {

    final String tableNameSuffix;

    /**
     * Protected constructor, called by subclasses.
     *
     * @param filterQuery The filter query which matches the subset of objects to be stored in this index.
     */
    protected PartialDiskIndex(Attribute<O, A> attribute, Query<O> filterQuery) {
        super(attribute, filterQuery);
        this.tableNameSuffix = "_partial_" + sanitizeForTableName(filterQuery.toString());
    }

    @Override
    @SuppressWarnings("unchecked") // unchecked, because type K will be provided later via the init() method
    protected SortedKeyStatisticsAttributeIndex<A, O> createBackingIndex() {
        return new DiskIndex(DiskPersistence.class, attribute, tableNameSuffix) {
            @Override
            public Index getEffectiveIndex() {
                return PartialDiskIndex.this.getEffectiveIndex();
            }
        };
    }

    // ---------- Static factory methods to create PartialDiskIndex ----------

    /**
     * Creates a new {@link PartialDiskIndex}. This will obtain details of the {@link DiskPersistence} to use from the
     * IndexedCollection, throwing an exception if the IndexedCollection has not been configured with a suitable
     * DiskPersistence.
     *
     * @param attribute The {@link Attribute} on which the index will be built.
     * @param filterQuery The filter query which matches the subset of objects to be stored in this index.
     * @param <A> The type of the attribute to be indexed.
     * @param <O> The type of the object containing the attribute.
     * @return A {@link DiskIndex} on the given attribute.
     */
    public static <A extends Comparable<A>, O> PartialDiskIndex<A, O> onAttributeWithFilterQuery(final Attribute<O, A> attribute, final Query<O> filterQuery) {
        return new PartialDiskIndex<A, O>(attribute, filterQuery);
    }
}
