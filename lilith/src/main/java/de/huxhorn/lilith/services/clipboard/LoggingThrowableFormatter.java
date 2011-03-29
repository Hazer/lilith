/*
 * Lilith - a log event viewer.
 * Copyright (C) 2007-2010 Joern Huxhorn
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
package de.huxhorn.lilith.services.clipboard;

import de.huxhorn.lilith.data.eventsource.EventWrapper;
import de.huxhorn.lilith.data.logging.ExtendedStackTraceElement;
import de.huxhorn.lilith.data.logging.LoggingEvent;
import de.huxhorn.lilith.data.logging.ThrowableInfo;

public class LoggingThrowableFormatter
	implements ClipboardFormatter
{
	private static final long serialVersionUID = 830054294833389446L;

	public String getName()
	{
		return "Copy Throwable";
	}

	public String getDescription()
	{
		return "Copies the Throwable of the logging event to the clipboard.";
	}

	public String getAccelerator()
	{
		return null;
	}

	public boolean isCompatible(Object object)
	{
		if(object instanceof EventWrapper)
		{
			EventWrapper wrapper = (EventWrapper) object;
			if(wrapper.getEvent() != null)
			{
				Object eventObj = wrapper.getEvent();
				if(eventObj instanceof LoggingEvent)
				{
					LoggingEvent loggingEvent = (LoggingEvent) eventObj;
					return loggingEvent.getThrowable() != null;
				}
			}
		}
		return false;
	}

	public String toString(Object object)
	{
		if(object instanceof EventWrapper)
		{
			EventWrapper wrapper = (EventWrapper) object;
			if(wrapper.getEvent() != null)
			{
				Object eventObj = wrapper.getEvent();
				if(eventObj instanceof LoggingEvent)
				{
					LoggingEvent loggingEvent = (LoggingEvent) eventObj;
					ThrowableInfo info = loggingEvent.getThrowable();
					if(info != null)
					{
						StringBuilder throwableText = new StringBuilder();
						for(; ;)
						{
							throwableText.append("Exception: ").append(info.getName()).append("\n");
							String message = info.getMessage();
							if(message != null && !message.equals(info.getName()))
							{
								throwableText.append("Message: ").append(message).append("\n");
							}
							ExtendedStackTraceElement[] st = info.getStackTrace();
							if(st != null)
							{
								throwableText.append("StackTrace:\n");
								for(ExtendedStackTraceElement current : st)
								{
									throwableText.append("\tat ").append(current.toString(true)).append("\n");
								}
							}
							info = info.getCause();
							if(info == null)
							{
								break;
							}
							throwableText.append("\nCaused by:\n");
						}
						return throwableText.toString();
					}
				}
			}
		}

		return null;
	}
}