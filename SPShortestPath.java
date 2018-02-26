/*
Name: Isiah Behner
Assignment: Programming Assingment 5
Course/Semester: CS 371 - Fall 2017
Instructor: Wolff
Sources consulted: Classmates and Professor
*/

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Scanner;

public class SPShortestPath {

    private PriorityQueue<Vertex> pqueue = new PriorityQueue<Vertex>(); //global queue
    private String[] cityNames; //list of city names
    private int[][] roads; //adjacency matrix
    private int numCities; //total number of cities

    /**
     * Contructor method that reads in the file data and does the entire
     * algorithm. Prints out the shortest path between the source and
     * the end city.
     * @param filename the name of the file
     */
	public SPShortestPath(String filename){
		File file = new File(filename);
        Scanner scan = null;
        try {
            scan = new Scanner(file);
        } catch (FileNotFoundException e) {
            System.out.println("This file does not exist.");
        }

        numCities = scan.nextInt(); //get number of cities
		scan.nextLine();

		//store the names of the cities in an array (index corresponds to city name)
		cityNames = new String[numCities];
		for(int i = 0; i < numCities; i++) {
			cityNames[i] = scan.nextLine();
		}

		//creating the matrix
		roads = new int[numCities][numCities];

		//loop over to add the data to the matrix
		while(!scan.hasNext(".")) {
			String temp = scan.nextLine().trim();
			roads = createMatrix(temp);
		}

		scan.nextLine();

		//looping in order to do the algorithm on each matrix given in the file
		while (scan.hasNext()) {

			//add the vertices to the priority queue
			for (int n = 0; n < numCities; n++) {
				int dist = Integer.MAX_VALUE;
				int parent = -1;
				Vertex vertex = new Vertex(n, parent, dist);
				pqueue.add(vertex); //adding the vertex to the pqueue
			}

			String closure = scan.nextLine(); //the closure
			String closeStart = closure.substring(0, closure.indexOf(",")).trim(); //the source
			String closeEnd = closure.substring(closure.indexOf(",") + 2).trim(); //the end

			int startInt = -10;
			int endInt = -10;

			for(int x = 0; x < numCities; x++){
			    if(cityNames[x].equals(closeStart)){
			        startInt = x;
                }
                else if(cityNames[x].equals(closeEnd)){
			        endInt = x;
                }
            }

            //the city doesn't exist
            if(startInt == -10){
			    System.out.println(closeStart + " is not a recognized city" + '\n');
            }
            else if(endInt == -10){
                System.out.println(closeEnd + " is not a recognized city" + '\n');
            }
            else{
                int beforeClosure = roads[startInt][endInt];
                //update matrix with closure
                roads[startInt][endInt] = 0;

                //call dijkstra's algorithm
                dijkstra(startInt, endInt);

                //reset the value for the matrix
                roads[startInt][endInt] = beforeClosure;
            }

		}
		
		scan.close();
	}//end of constructor

    /**
     * Method that creates the adjacency matrix
     * @param info one line of the file
     * @return the matrix filled with the data from the file
     */
	private int[][] createMatrix(String info) {
		
		int first = info.indexOf(",");
		String start = info.substring(0, first).trim(); //first city
		String end = info.substring(first + 2, info.lastIndexOf(",")).trim(); //second city
		String dist = info.substring(info.lastIndexOf(",") + 2).trim(); //distance between the cities
		int distance = Integer.parseInt(dist);
		
		int startIndex = 0, endIndex = 0;
		for(int k = 0; k < numCities; k++) {
			if(cityNames[k].contains(start)) {
				startIndex = k;
			}
			else if(cityNames[k].contains(end)) {
				endIndex = k;
			}
		}
		
		roads[startIndex][endIndex] = distance;
		roads[endIndex][startIndex] = distance;
		
		return roads;
	}

    /**
     * Private method that performs dijkstra's algorithm
     * @param startCity the source
     * @param endCity the end
     */
	private void dijkstra(int startCity, int endCity) {
        boolean[] intree = new boolean[numCities];
        int finalDist = 0; //keep the end distance
        ArrayList<Vertex> used = new ArrayList<Vertex>();
        ArrayList<Vertex> printingList = new ArrayList<Vertex>(); //list used for printing the backtrack

        //initialize a boolean array to all false to represent whether avertex has been added to a tree
        for(int nums = 0; nums < numCities; nums++){
            intree[nums] = false;
        }

        Vertex src = new Vertex(startCity, -1, 0);
        pqueue.add(src); //add the updated src vertex to the queue

        //while the end city hasn't been added to the tree, loop
        while(!intree[endCity]){
            Vertex smallestDist = pqueue.poll(); //take off the smallest element in the pqueue
            used.add(smallestDist); //add this to the arraylist to be used for backtracking
            int cityNumber = smallestDist.getNumber(); //get the city number

            //loop to get the neighbors and add them to the queue
            for(int cityCol = 0; cityCol < numCities; cityCol++){
                //if conditions are met, the valid path
                if(roads[cityNumber][cityCol] != 0 && intree[cityCol] != true){
                    int newDist = roads[cityNumber][cityCol] + smallestDist.getDistance();
                    Vertex v = new Vertex(cityCol, cityNumber, newDist);
                    pqueue.add(v);
                }
            }

            intree[cityNumber] = true; //added to tree

            //if we found the goal
            if(smallestDist.getNumber() == endCity) {
                printingList.add(smallestDist);
                int v = smallestDist.getParent();
                int d = 0;
                while(!used.isEmpty() && d < used.size()){
                    if(used.get(d).getNumber() == v){
                        printingList.add(used.get(d));
                        v = used.get(d).getParent();
                        used.remove(d);
                        d = -1;
                    }
                    d++;
                }
                finalDist = smallestDist.getDistance();
            }
        }//end of while

        //loop to print out the path
        for(int i = 0; i < printingList.size(); i++){
            int pparent = printingList.get(i).getParent();
            if(pparent != -1) {
                System.out.print(cityNames[printingList.get(i).getNumber()] + " <--> ");
            }
            else{
                System.out.println(cityNames[printingList.get(i).getNumber()]);
            }
        }

        System.out.println("Total Distance: " + finalDist + " Miles" + '\n');
	}
}//end of class
