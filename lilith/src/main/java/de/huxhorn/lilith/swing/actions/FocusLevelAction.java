/*
 * Lilith - a log event viewer.
 * Copyright (C) 2007-2013 Joern Huxhorn
 *
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
package de.huxhorn.lilith.swing.actions;

import de.huxhorn.lilith.conditions.LevelCondition;
import de.huxhorn.lilith.data.logging.LoggingEvent;
import de.huxhorn.sulky.conditions.Condition;

import javax.swing.*;

public class FocusLevelAction
		extends FilterBaseAction
{
	private static final long serialVersionUID = -7615646386307125745L;

	private final LoggingEvent.Level level;

	public FocusLevelAction(LoggingEvent.Level level)
	{
		super(level.name());
		this.level = level;
		putValue(Action.SHORT_DESCRIPTION, resolveCondition().toString());
		setViewContainer(null);
	}

	@Override
	protected void updateState()
	{
		if(viewContainer == null)
		{
			setEnabled(false);
			return;
		}
		setEnabled(true);
	}

	@Override
	protected Condition resolveCondition()
	{
		return new LevelCondition(level.name());
	}
}
