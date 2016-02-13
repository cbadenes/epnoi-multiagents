/*
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.epnoi.multiagents;

import org.epnoi.model.domain.Relation;
import org.epnoi.model.domain.Resource;
import org.epnoi.multiagents.domain.Agent;
import org.epnoi.multiagents.domain.DocumentAgent;
import org.epnoi.multiagents.domain.TopicAgent;
import org.epnoi.storage.UDM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sim.engine.Schedule;
import sim.engine.SimState;
import sim.engine.Steppable;
import sim.field.continuous.Continuous2D;

import java.util.HashMap;

/**
 * Created by cbadenes on 05/12/14.
 */
public class EpnoiMA extends SimState {

    private static final Logger LOG = LoggerFactory.getLogger(EpnoiMA.class);

    public double width             = 7.0;      // 7.0
    public double height            = 7.0;      // 7.0
    public double agentRadius       = 0.15;     // 0.06

    public Continuous2D space;
    public Continuous2D area;

    private TopicAgent[] topicAgents;
    private DocumentAgent[] documentAgents;


    private double range            = 6.0;  // 0.8, 3.5
    private double maxVelocity      = 0.15;     // 0.06

    private int movementHistory     = 3;

    // Properties
    public double getRange() {
        return range;
    }
    public void setRange(double range) {
        this.range = range;
    }
    public double getMaxVelocity() {
        return maxVelocity;
    }
    public void setMaxVelocity(double maxVelocity) {
        this.maxVelocity = maxVelocity;
    }
    public int getMovementHistory() {
        return movementHistory;
    }
    public void setMovementHistory(int movementHistory) {
        this.movementHistory = movementHistory;
    }

    private UDM udm;

    public EpnoiMA(long seed) {
        super(seed);
    }

    public void setUdm(UDM udm){
        this.udm = udm;
    }

    private Agent initializeParticle(final Agent agent){
//        schedule.scheduleRepeating(Schedule.EPOCH, 1, new Steppable() {
//            public void step(SimState state) {
//                agent.stepUpdateRadiation();
//            }
//        });

        schedule.scheduleRepeating(Schedule.EPOCH, 2, new Steppable() {
            public void step(SimState state) {
                agent.stepUpdateVelocity();
            }
        });

        schedule.scheduleRepeating(Schedule.EPOCH, 3, new Steppable() {
            public void step(SimState state) {
                agent.stepUpdatePosition();
            }
        });


        return agent;
    }

    public void start() {
        super.start();

        space = new Continuous2D(0.01, width, height);
        area  = new Continuous2D(0.04, width, height); // radioactive particles

        // Load topics from DB

        Iterable<Relation> result = udm.find(Relation.Type.DOCUMENT_DEALS_WITH_TOPIC).in(Resource.Type.DOMAIN, "http://epnoi.org/domains/382130c5-1d84-4b21-a591-90d2c235f0a5");

        HashMap<String,TopicAgent> topics       = new HashMap<String, TopicAgent>();
        HashMap<String,DocumentAgent> documents = new HashMap<String, DocumentAgent>();

//        topics.put("t1",new TopicAgent(this,"t1"));
//        topics.put("t2",new TopicAgent(this,"t2"));
//
//        DocumentAgent documentAgent = new DocumentAgent(this, "d1");
//        documentAgent.addWeight("t1",0.5);
//        documentAgent.addWeight("t2",0.5);
//        documents.put("d1",documentAgent);


        for (Relation relation : result) {

            Resource topic = relation.getEnd();
            TopicAgent topicAgent = topics.get(topic.getUri());
            if (topicAgent == null){
                topicAgent = new TopicAgent(this, topic.getUri());
            }
            topics.put(topic.getUri(),topicAgent);

            Resource document = relation.getStart();
            DocumentAgent documentAgent = documents.get(document.getUri());
            if (documentAgent == null){
                documentAgent = new DocumentAgent(this, document.getUri());
            }
            documentAgent.addWeight(topic.getUri(),relation.getWeight());
            documents.put(document.getUri(),documentAgent);

        }

        int numTopics = topics.keySet().size();
        int numDocuments = documents.keySet().size();

        LOG.info("NUmTopics: " + numTopics);
        LOG.info("NUmDocuments: " + numDocuments);

        int index = 0;
        topicAgents = new TopicAgent[numTopics];
        for (String key: topics.keySet()){
            LOG.info("added topic: " + topics.get(key));
            topicAgents[index++] = (TopicAgent) initializeParticle(topics.get(key));
        }

        index = 0;
        documentAgents = new DocumentAgent[numDocuments];
        for (String key: documents.keySet()){
            LOG.info("added document: " + documents.get(key));
            documentAgents[index++] = (DocumentAgent) initializeParticle(documents.get(key));
        }

    }

    public static void main(String[] args) {
        doLoop(EpnoiMA.class, args);
        System.exit(0);
    }

}
