/*
 * Licensed to Elasticsearch under one or more contributor
 * license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright
 * ownership. Elasticsearch licenses this file to you under
 * the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.elasticsearch.search.aggregations.metrics.percentiles.hdr;

import org.elasticsearch.search.aggregations.Aggregator;
import org.elasticsearch.search.aggregations.AggregatorFactories;
import org.elasticsearch.search.aggregations.AggregatorFactory;
import org.elasticsearch.search.aggregations.InternalAggregation.Type;
import org.elasticsearch.search.aggregations.pipeline.PipelineAggregator;
import org.elasticsearch.search.aggregations.support.AggregationContext;
import org.elasticsearch.search.aggregations.support.ValuesSource;
import org.elasticsearch.search.aggregations.support.ValuesSource.Numeric;
import org.elasticsearch.search.aggregations.support.ValuesSourceAggregatorFactory;
import org.elasticsearch.search.aggregations.support.ValuesSourceConfig;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class HDRPercentileRanksAggregatorFactory
        extends ValuesSourceAggregatorFactory<ValuesSource.Numeric, HDRPercentileRanksAggregatorFactory> {

    private final double[] values;
    private final int numberOfSignificantValueDigits;
    private final boolean keyed;

    public HDRPercentileRanksAggregatorFactory(String name, Type type, ValuesSourceConfig<Numeric> config, double[] values,
            int numberOfSignificantValueDigits, boolean keyed, AggregationContext context, AggregatorFactory<?> parent,
            AggregatorFactories.Builder subFactoriesBuilder, Map<String, Object> metaData) throws IOException {
        super(name, type, config, context, parent, subFactoriesBuilder, metaData);
        this.values = values;
        this.numberOfSignificantValueDigits = numberOfSignificantValueDigits;
        this.keyed = keyed;
    }

    @Override
    protected Aggregator createUnmapped(Aggregator parent, List<PipelineAggregator> pipelineAggregators, Map<String, Object> metaData)
            throws IOException {
        return new HDRPercentileRanksAggregator(name, null, context, parent, values, numberOfSignificantValueDigits, keyed,
                config.format(), pipelineAggregators, metaData);
    }

    @Override
    protected Aggregator doCreateInternal(Numeric valuesSource, Aggregator parent, boolean collectsFromSingleBucket,
            List<PipelineAggregator> pipelineAggregators, Map<String, Object> metaData) throws IOException {
        return new HDRPercentileRanksAggregator(name, valuesSource, context, parent, values, numberOfSignificantValueDigits, keyed,
                config.format(), pipelineAggregators, metaData);
    }

}