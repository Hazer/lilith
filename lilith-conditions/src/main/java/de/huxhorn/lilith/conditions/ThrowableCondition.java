/*
 * Lilith - a log event viewer.
 * Copyright (C) 2007-2016 Joern Huxhorn
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
package de.huxhorn.lilith.conditions;

import de.huxhorn.lilith.data.eventsource.EventWrapper;
import de.huxhorn.lilith.data.logging.LoggingEvent;
import de.huxhorn.lilith.data.logging.ThrowableInfo;

public class ThrowableCondition
	implements LilithCondition, SearchStringCondition
{
	private static final long serialVersionUID = 478148397663546849L;

	public static final String DESCRIPTION = "Throwable";

	private String searchString;

	public ThrowableCondition()
	{
		this(null);
	}

	public ThrowableCondition(String searchString)
	{
		setSearchString(searchString);
	}

	public void setSearchString(String searchString)
	{
		if(searchString != null)
		{
			searchString = searchString.trim();
			if(searchString.length() == 0)
			{
				searchString = null;
			}
		}
		this.searchString = searchString;
	}

	public String getSearchString()
	{
		return searchString;
	}

	public boolean isTrue(Object value)
	{
		if(value instanceof EventWrapper)
		{
			EventWrapper wrapper = (EventWrapper) value;
			Object eventObj = wrapper.getEvent();
			if(eventObj instanceof LoggingEvent)
			{
				LoggingEvent event = (LoggingEvent) eventObj;

				ThrowableInfo throwable = event.getThrowable();
				if(throwable == null)
				{
					return false;
				}
				if(searchString == null)
				{
					// no search string means "match any Throwable"
					return true;
				}
				if(searchString.equals(throwable.getName()))
				{
					// otherwise match if name matches
					return true;
				}

			}
		}
		return false;
	}

	public ThrowableCondition clone()
		throws CloneNotSupportedException
	{
		ThrowableCondition result = (ThrowableCondition) super.clone();
		result.setSearchString(searchString);
		return result;
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		ThrowableCondition that = (ThrowableCondition) o;

		return searchString != null ? searchString.equals(that.searchString) : that.searchString == null;

	}

	@Override
	public int hashCode()
	{
		return searchString != null ? searchString.hashCode() : 0;
	}

	public String getDescription()
	{
		return DESCRIPTION;
	}

	@Override
	public String toString()
	{
		StringBuilder result = new StringBuilder();
		result.append(getDescription());
		result.append("(");
		if(searchString != null)
		{
			result.append(searchString);
		}
		result.append(")");
		return result.toString();
	}
}
