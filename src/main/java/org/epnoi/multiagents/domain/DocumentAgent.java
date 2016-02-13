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

package org.epnoi.multiagents.domain;

import org.epnoi.multiagents.EpnoiMA;
import sim.util.Bag;
import sim.util.Double2D;
import sim.util.MutableDouble2D;

import java.awt.*;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by cbadenes on 12/12/14.
 */
public class DocumentAgent extends Agent {

//    protected TopicAgent source;


    private HashMap<String,Double> weights;


    public DocumentAgent(EpnoiMA sim, String id) {
        super(
                sim.space.stx((sim.random.nextDouble() * sim.width) - (sim.width * 0.5)),
                sim.space.sty((sim.random.nextDouble() * sim.height) - (sim.height * 0.5)),
                (sim.random.nextDouble() % sim.getMaxVelocity()),
                (sim.random.nextDouble() % sim.getMaxVelocity()),
                sim,
                id,
                1.0   // lambda response rate
        );

        this.weights = new HashMap<String, Double>();
    }

    public void addWeight(String id, Double value){
        this.weights.put(id,value);
    }

    @Override
    public Color getColor() {
        float[] colors = new float[3];
        Color.RGBtoHSB(0,255,0,colors);
        return Color.getHSBColor(colors[0],colors[1],colors[2]);
    }

    @Override
    public void stepUpdateVelocity(){
        this.velocity = new MutableDouble2D(0.0,0.0);
        MutableDouble2D displacement = new MutableDouble2D(0.0,0.0);

        //Bag neighbors = sim.space.getNeighborsExactlyWithinDistance(new Double2D(position), sim.radiationRadius);
        Bag neighbors = sim.space.getNeighborsWithinDistance(new Double2D(position), sim.getRange());
        if (neighbors.size() > 1){
            Iterator iterator = neighbors.iterator();
            while(iterator.hasNext()){
                Agent agent = (Agent) iterator.next();

                if (agent.id.equals(this.id)) continue;

                Double2D force = new Double2D(0.0,0.0);

                if ((agent.id.equals(this.id))) continue;

                if (agent instanceof TopicAgent){
                    // Far away from other topics

                    force = calculateDisplacementBy(agent.position, weights.get(agent.id)*distance(agent.position, this.position));
                }

                if (agent instanceof DocumentAgent){
                    // nothing to do
                }

                //if (distance(agent.position, this.position) <= sim.agentRadius) continue;

                displacement.addIn(force);

            }
        }
        if (displacement.length() == 0) {
            // random movement
            MutableDouble2D endpoint = new MutableDouble2D();
            MutableDouble2D movement = new MutableDouble2D();
            do{
                endpoint.setTo(this.position);
                movement.setTo(randomMovement());
                endpoint.addIn(movement);
            }while(!moveFrom(endpoint, sim.getMaxVelocity()));
            displacement.addIn(movement);

        }


        MutableDouble2D partialVelocity = limitToMaxVelocity(displacement, sim.getMaxVelocity()*responseRate);
        this.velocity.addIn(partialVelocity);
    }




//    @Override
//    public void stepUpdatePosition(){
//
//        MutableDouble2D startingPoint = this.position.dup();
//
//        if (velocity.length() > 0 ){
//
//            // Move
//            position.addIn(velocity);
//
//            // Adjust to toroidal space
//            this.position.x = sim.space.stx(position.x);
//            this.position.y = sim.space.sty(position.y);
//
//            // Avoid collision moving backward
//            Bag neighbours = sim.space.getNeighborsExactlyWithinDistance(new Double2D(this.position), sim.agentRadius*10, true);
//            Double2D backward = new Double2D(-this.velocity.x/10,-this.velocity.y/10);
//            while (neighbours.size() > 0){
//                this.position.addIn(backward);
//                if (neighbours.contains(this) && neighbours.size()==1) break;
//                neighbours = sim.space.getNeighborsExactlyWithinDistance(new Double2D(this.position), sim.agentRadius*10, true);
//            }
//        }
//
//        // Maintain a maximum distance to radioactive particle:: Gas Particle Model
//        //double distance = (this.source != null)? distance(this.position, this.source.position) : 0.0 ;
////        if ((distance > sim.radiationRadius)
////                && (distance > sim.radiationRadius*this.source.attached)
////                && (distance < (this.source.attached*sim.radiationRadius+sim.getMaxVelocity()))
////                && (source.velocity.length() <= 0.0)
////                ){
////            // undo movement
////            this.position = startingPoint;
////            // random movement
////            this.velocity = randomMovement();
////            // evaluate
////            stepUpdatePosition();
////            return;
////        }
//
//        sim.space.setObjectLocation(this, new Double2D(position));
//        this.lastMovements.add(new Double2D(this.position));
//        this.velocity.setTo(0.0,0.0);
//    }


//    @Override
//    public void stepUpdateRadiation() {
//        // maintain intensity constant
//    }

//    private synchronized void attach(TopicAgent particle){
//        // Attach to radioactive particles within the radiation radius
//        if ((distance(particle.position, this.position) < sim.radiationRadius)){
//            this.source = particle;
//        }
//    }
}
