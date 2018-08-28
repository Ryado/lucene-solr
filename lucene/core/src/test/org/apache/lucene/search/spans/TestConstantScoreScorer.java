/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.lucene.search.spans;


import java.util.Arrays;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.NoMergePolicy;
import org.apache.lucene.search.ConstantScoreScorer;
import org.apache.lucene.search.DocIdSetIterator;
import org.apache.lucene.search.LeafCollector;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.LuceneTestCase;


public class TestConstantScoreScorer extends LuceneTestCase {

  public void testSetMinCompetitiveScore() throws Exception {






    Directory dir = newDirectory();
    IndexWriter w = new IndexWriter(dir, newIndexWriterConfig().setMergePolicy(NoMergePolicy.INSTANCE));
    Document doc = new Document();
    w.addDocuments(Arrays.asList(doc, doc, doc, doc));
    w.flush();
    w.addDocuments(Arrays.asList(doc, doc));
    w.flush();
    IndexReader reader = DirectoryReader.open(w);
    w.close();

    TopScoreDocCollector collector = TopScoreDocCollector.create(2, null, 1);
    ConstantScoreScorer scorer = new ConstantScoreScorer(null, 3, DocIdSetIterator.all(2));

    LeafCollector leafCollector = collector.getLeafCollector(reader.leaves().get(0));
    leafCollector.setScorer(scorer);

    System.out.println(collector.scoreMode().toString());

    System.out.println("+++++");
    leafCollector.collect(0);
    System.out.println("DOC:"+ scorer.iterator().nextDoc());
    System.out.println("Score:"+ scorer.score()+" | minCompetitiveScore: "+scorer.getMinCompetitiveScore());
    leafCollector.collect(8);
    System.out.println("DOC:"+ scorer.iterator().nextDoc());
    System.out.println("Score:"+ scorer.score()+" | minCompetitiveScore: "+scorer.getMinCompetitiveScore());
    leafCollector.collect(10);
    System.out.println("DOC:"+ scorer.iterator().nextDoc());
    System.out.println("Score:"+ scorer.score()+" | minCompetitiveScore: "+scorer.getMinCompetitiveScore());



    assertEquals(Math.nextUp(3f), 90f, 0f);


   reader.close();
   dir.close();

  }

}
