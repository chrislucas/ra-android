/*
 * Copyright (C) 2014 BeyondAR
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.br.ar.beyondar.util.math.geom;

import com.beyondar.android.util.math.geom.*;
import com.beyondar.android.util.math.geom.Plane;
import com.beyondar.android.util.math.geom.Point2;
import com.beyondar.android.util.math.geom.Point3;

/**
 * Represents a geometric triangle
 */
public class Triangle {

	private com.beyondar.android.util.math.geom.Point3 p1, p2, p3;

	private com.beyondar.android.util.math.geom.Vector3 normal;

	/**
	 * Constructs a triangle from 3 points
	 * 
	 * @param p1
	 *            Point 1
	 * @param p2
	 *            Point 2
	 * @param p3
	 *            Point 3
	 */
	public Triangle(com.beyondar.android.util.math.geom.Point3 p1, com.beyondar.android.util.math.geom.Point3 p2, com.beyondar.android.util.math.geom.Point3 p3) {
		this.p1 = p1;
		this.p2 = p2;
		this.p3 = p3;
		this.normal = com.beyondar.android.util.math.geom.Vector3.normalVector(p1, p2, p3);
	}

	/**
	 * Returns the plain that contains this triangle
	 * 
	 * @return the plain that contains this triangle
	 */
	public com.beyondar.android.util.math.geom.Plane getPlane() {
		return new Plane(p1, p2, p3);
	}

	/**
	 * Returns if the given point is contained in this triangle.
	 * 
	 * @param p
	 *            the point. It must be contained in the same plane as the
	 *            triangle
	 * @return <b>true</b> if the point is contained by the triangle,
	 *         <b>false</b> otherwise
	 */
	public boolean contains(com.beyondar.android.util.math.geom.Point3 p) {
		int max = normal.getGreatestComponent();
		com.beyondar.android.util.math.geom.Point2 p1 = new com.beyondar.android.util.math.geom.Point2(this.p1, max);
		com.beyondar.android.util.math.geom.Point2 p2 = new com.beyondar.android.util.math.geom.Point2(this.p2, max);
		com.beyondar.android.util.math.geom.Point2 p3 = new com.beyondar.android.util.math.geom.Point2(this.p3, max);
		com.beyondar.android.util.math.geom.Point2 i = new com.beyondar.android.util.math.geom.Point2(p, max);

		return (checkSide(p1, p2, i) == checkSide(p2, p3, i))
				&& (checkSide(p1, p2, i) == checkSide(p3, p1, i));
	}

	private int checkSide(com.beyondar.android.util.math.geom.Point2 o, com.beyondar.android.util.math.geom.Point2 d, Point2 p) {
		return (int) Math.signum(-(d.y - o.y) * (p.x - o.x) + (d.x - o.x)
				* (p.y - o.y));
	}

	/**
	 * Returns a 3 elements array with the points
	 * 
	 * @return a 3 elements array with the points
	 */
	public com.beyondar.android.util.math.geom.Point3[] getPoints() {
		return new Point3[] { p1, p2, p3 };
	}

}
