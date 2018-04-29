/*
 * Lilith - a log event viewer.
 * Copyright (C) 2007-2017 Joern Huxhorn
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

import de.huxhorn.lilith.data.logging.LoggingEvent;
import de.huxhorn.lilith.data.logging.Marker;
import de.huxhorn.lilith.swing.LilithActionId;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static de.huxhorn.lilith.services.clipboard.FormatterTools.resolveLoggingEvent;

public class LoggingMarkerFormatter
		extends AbstractNativeClipboardFormatter
{
	private static final long serialVersionUID = -4044572511731233804L;

	public LoggingMarkerFormatter()
	{
		super(LilithActionId.COPY_MARKER);
	}

	@Override
	public boolean isCompatible(Object object)
	{
		return resolveLoggingEvent(object).map(it -> it.getMarker() != null).orElse(false);
	}

	@Override
	public String toString(Object object)
	{
		return resolveLoggingEvent(object).map(LoggingMarkerFormatter::toStringOrNull).orElse(null);
	}

	private static String toStringOrNull(LoggingEvent loggingEvent)
	{
		Marker marker = loggingEvent.getMarker();
		if (marker == null)
		{
			return null;
		}
		StringBuilder text = new StringBuilder();
		buildMarker(text, 0, marker, new HashSet<>());
		return text.toString();
	}

	private static void buildMarker(StringBuilder text, int indent, Marker marker, Set<String> handledMarkers)
	{
		for (int i = 0; i < indent; i++)
		{
			text.append("  ");
		}
		String markerName = marker.getName();
		text.append("- ").append(markerName);
		if (handledMarkers.contains(markerName))
		{
			text.append(" [..]\n");
		}
		else
		{
			text.append('\n');
			handledMarkers.add(markerName);
			Map<String, Marker> references = marker.getReferences();
			if (references != null)
			{
				for (Map.Entry<String, Marker> current : references.entrySet())
				{
					buildMarker(text, indent + 1, current.getValue(), handledMarkers);
				}
			}
		}
	}
}
