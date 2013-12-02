/*******************************************************************************
 * CogTool Copyright Notice and Distribution Terms
 * CogTool 1.2, Copyright (c) 2005-2013 Carnegie Mellon University
 * This software is distributed under the terms of the FSF Lesser
 * Gnu Public License (see LGPL.txt). 
 * 
 * CogTool is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 * 
 * CogTool is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with CogTool; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 * 
 * CogTool makes use of several third-party components, with the 
 * following notices:
 * 
 * Eclipse SWT
 * Eclipse GEF Draw2D
 * 
 * Unless otherwise indicated, all Content made available by the Eclipse 
 * Foundation is provided to you under the terms and conditions of the Eclipse 
 * Public License Version 1.0 ("EPL"). A copy of the EPL is provided with this 
 * Content and is also available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * CLISP
 * 
 * Copyright (c) Sam Steingold, Bruno Haible 2001-2006
 * This software is distributed under the terms of the FSF Gnu Public License.
 * See COPYRIGHT file in clisp installation folder for more information.
 * 
 * ACT-R 6.0
 * 
 * Copyright (c) 1998-2007 Dan Bothell, Mike Byrne, Christian Lebiere & 
 *                         John R Anderson. 
 * This software is distributed under the terms of the FSF Lesser
 * Gnu Public License (see LGPL.txt).
 * 
 * Apache Jakarta Commons-Lang 2.1
 * 
 * This product contains software developed by the Apache Software Foundation
 * (http://www.apache.org/)
 * 
 * jopt-simpler
 * 
 * Copyright (c) 2004-2013 Paul R. Holser, Jr.
 * 
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 * 
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 * 
 * Mozilla XULRunner 1.9.0.5
 * 
 * The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/.
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 * 
 * The J2SE(TM) Java Runtime Environment
 * 
 * Copyright 2009 Sun Microsystems, Inc., 4150
 * Network Circle, Santa Clara, California 95054, U.S.A.  All
 * rights reserved. U.S.  
 * See the LICENSE file in the jre folder for more information.
 ******************************************************************************/

package edu.cmu.cs.hcii.cogtool.uimodel;

import org.eclipse.draw2d.IFigure;

import edu.cmu.cs.hcii.cogtool.model.ChildWidget;
import edu.cmu.cs.hcii.cogtool.model.AParentWidget;
import edu.cmu.cs.hcii.cogtool.model.SimpleWidgetGroup;

public abstract class AbstractGraphicalParentWidget<P extends AParentWidget,
                                             C extends ChildWidget>
                                       extends GraphicalWidgetBase<P>
                                       implements GraphicalParentWidget<P, C>
{
    // Support for finding the figure for a widget, next/prev in a widget group
    protected WidgetFigureSupport figureSpt;

    public AbstractGraphicalParentWidget(P m,
                                  int color,
                                  boolean supportToolTip,
                                  int rolloverCursor,
                                  FrameUIModel displayComputer,
                                  WidgetFigureSupport spt,
                                  DefaultSEUIModel ovr)
    {
        super(m, color, supportToolTip, rolloverCursor, displayComputer, ovr);

        this.figureSpt = spt;
    }


	@SuppressWarnings("unchecked")
    public GraphicalChildWidget<P, C> getFigureAt(int index)
    {
        AParentWidget parentWidget = model;
        ChildWidget child = parentWidget.getItem(index);

        if (child == null) {
            return null;
        }

        return (GraphicalChildWidget<P, C>) this.figureSpt.getWidgetFigure(child);
    }


    public void setChildrenVisible(boolean visible)
    {
        AParentWidget parentWidget = model;
        int numChildren = parentWidget.itemCount();

        for (int i = 0; i < numChildren; i++) {
            GraphicalWidget<?> gw =
                this.figureSpt.getWidgetFigure(parentWidget.getItem(i));

            if (gw != null) {
                gw.setVisible(visible);

                IFigure parent = gw.getParent();

                if (visible && (parent != null)) {
                    parent.add(gw, gw.getBounds(), -1);
                }
            }
        }
    }


    public void openChildren()
    {
        AParentWidget menuWidget = model;

        if (menuWidget.itemCount() > 0) {
            setChildrenVisible(true);
        }
    }


    public void closeChildren()
    {
        AParentWidget menuWidget = model;

        if (menuWidget.itemCount() > 0) {
            setChildrenVisible(false);
        }
    }


    public boolean canHaveChildren()
    {
        return ((AParentWidget) model).canHaveChildren();
    }


    public boolean hasChildren()
    {
        return ((AParentWidget) model).hasChildren();
    }

    protected GraphicalWidget<?> selectChild()
    {
        AParentWidget hdr = getModel();
        SimpleWidgetGroup childItems = hdr.getChildren();

        if ((childItems != null) && (childItems.size() > 0)) {
            return this.figureSpt.getWidgetFigure(childItems.get(0));
        }

        return null;
    }
}