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
import java.util.Iterator;

/**
 * Created by cbadenes on 05/12/14.
 */
public class TopicAgent extends Agent {

    public TopicAgent(EpnoiMA sim, String id) {
        super(
                sim.space.stx((sim.random.nextDouble() * sim.width) - (sim.width * 0.5)),
                sim.space.sty((sim.random.nextDouble() * sim.height) - (sim.height * 0.5)),
                (sim.random.nextDouble() % sim.getMaxVelocity()),
                (sim.random.nextDouble() % sim.getMaxVelocity()),
                sim,
                id,
                0.50                            // 0.15 lambda response rate
        );
        sim.area.setObjectLocation(this,new Double2D(position));
    }

    @Override
    public Color getColor() {
//        if (this.mode.equals(Mode.BLOCKED)) return Color.white;
        float[] colors = new float[3];
        Color.RGBtoHSB(255,0,0,colors);
        return Color.getHSBColor(colors[0],colors[1],colors[2]);
    }


    @Override
    public void stepUpdateVelocity(){

        MutableDouble2D displacement = new MutableDouble2D(0.0,0.0);

        Bag neighbors = sim.area.getNeighborsExactlyWithinDistance(new Double2D(position), sim.getRange());
        if (neighbors.size() > 1){
            Iterator iterator = neighbors.iterator();
            while(iterator.hasNext()){
                Agent agent = (Agent) iterator.next();

                Double2D force = new Double2D(0.0,0.0);

                if ((agent.id.equals(this.id))) continue;

                if (agent instanceof TopicAgent){
                    // Far away from other topics
                    //force = calculateDisplacementBy(agent.position, -1/distance(agent.position, this.position));
                }

                if (agent instanceof DocumentAgent){
                    // nothing to do
                }

                //if (distance(agent.position, this.position) <= sim.agentRadius) continue;

                displacement.addIn(force);
            }
        }
//        if (displacement.length() == 0) {
//            // random movement
//            MutableDouble2D endpoint = new MutableDouble2D();
//            MutableDouble2D movement = new MutableDouble2D();
//            do{
//                endpoint.setTo(this.position);
//                movement.setTo(randomMovement());
//                endpoint.addIn(movement);
//            }while(!moveFrom(endpoint, sim.getMaxVelocity()));
//            displacement.addIn(movement);
//
//        }

        MutableDouble2D limitedVelocity = limitToMaxVelocity(displacement, sim.getMaxVelocity()*responseRate);
        this.velocity.setTo(limitedVelocity);
    }

//    public void stepUpdatePosition(){
//        if (velocity.length() > 0 ){
//
//            Double2D startingPoint =  new Double2D(this.position);
//
//            // Move
//            position.addIn(velocity);
//
//            // Adjust to toroidal space
//            this.position.x = sim.space.stx(position.x);
//            this.position.y = sim.space.sty(position.y);
//
//            // Check neighbours
//            Bag neighbours = sim.space.getNeighborsExactlyWithinDistance(new Double2D(this.position), sim.getRange(), true);
//            if ((neighbours.size() > 0)) {
//                Iterator iterator = neighbours.iterator();
//                while(iterator.hasNext()){
//                    Agent agent = (Agent) iterator.next();
//                    if (agent.id.equals(this.id)) continue;
//                    if ((agent instanceof TopicAgent)){
//                        // Maybe creation of isolated groups of radioactive particles
//                        this.position.setTo(startingPoint);
//                        this.velocity.setTo(new Double2D(0.0,0.0));
//                        return;
//                    }else if (agent instanceof DocumentAgent){
//                        // Generate repulsive force to neighbours
//                        //agent.velocity.addIn(this.velocity);
//                    }
//                }
//            }
//        }
//
//        sim.space.setObjectLocation(this, new Double2D(position));
//        sim.area.setObjectLocation(this,new Double2D(position));
//    }


//    @Override
//    public void stepUpdateRadiation() {
//        if (this.mode.equals(Mode.RADIATE)){
//            // Update radiation based on neighbours
//            this.intensity = sim.getRadiationIntensity();
//            Bag neighbors = sim.space.getNeighborsExactlyWithinDistance(new Double2D(position), sim.radiationRadius,true);
//            Iterator iterator = neighbors.iterator();
//            while(iterator.hasNext()){
//                Agent agent = (Agent) iterator.next();
//                if (agent.id.equals(this.id)) continue;
////                if (agent instanceof DocumentAgent){
////                    DocumentAgent insulating = (DocumentAgent) agent;
////                    this.intensity -= ((insulating.source != null) && (insulating.source.id.equals(this.id)))?  Math.abs(agent.intensity) : 0 ;
////                }
//            }
//            if (this.intensity <= 0){
//                // Change to BLOCKED Mode
//                this.mode = Mode.BLOCKED;
//            }
//        }
//    }


}
