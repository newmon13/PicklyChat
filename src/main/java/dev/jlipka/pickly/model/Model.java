/*
 * Copyright (C) 2022 Parisi Alessandro
 * This file is part of MaterialFX (https://github.com/palexdev/MaterialFX).
 *
 * MaterialFX is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * MaterialFX is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with MaterialFX.  If not, see <http://www.gnu.org/licenses/>.
 */

package dev.jlipka.pickly.model;

import io.github.palexdev.materialfx.utils.FXCollectors;
import io.github.palexdev.mfxresources.fonts.IconDescriptor;
import io.github.palexdev.mfxresources.fonts.fontawesome.FontAwesomeBrands;
import io.github.palexdev.mfxresources.fonts.fontawesome.FontAwesomeRegular;
import io.github.palexdev.mfxresources.fonts.fontawesome.FontAwesomeSolid;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.stream.IntStream;

import static dev.jlipka.pickly.model.Device.State.OFFLINE;
import static dev.jlipka.pickly.model.Device.State.ONLINE;
import static dev.jlipka.pickly.model.Device.randomID;

public class Model {
	public static final IconDescriptor[] notificationsIcons;
	public static final ObservableList<User> users;

	static {
		notificationsIcons = new IconDescriptor[]{
				FontAwesomeSolid.BELL, FontAwesomeRegular.BELL,
				FontAwesomeSolid.CALENDAR, FontAwesomeSolid.CALENDAR_DAYS,
				FontAwesomeSolid.CHART_PIE, FontAwesomeSolid.CIRCLE, FontAwesomeRegular.CIRCLE,
				FontAwesomeSolid.CIRCLE_EXCLAMATION, FontAwesomeSolid.TRIANGLE_EXCLAMATION,
				FontAwesomeSolid.GEAR, FontAwesomeBrands.GOOGLE_DRIVE, FontAwesomeSolid.HOUSE,
				FontAwesomeSolid.CIRCLE_INFO, FontAwesomeSolid.MUSIC,
				FontAwesomeSolid.USER, FontAwesomeSolid.USERS, FontAwesomeSolid.VIDEO,
				FontAwesomeSolid.CIRCLE_XMARK
		};

		users = FXCollections.observableArrayList(
				User.ofSplit("Turner Romero", " ").randomAge(),
				User.ofSplit("Harley Hays", " ").randomAge(),
				User.ofSplit("Jeffrey Cannon", " ").randomAge(),
				User.ofSplit("Simeon Huang", " ").randomAge(),
				User.ofSplit("Jennifer Donovan", " ").randomAge(),
				User.ofSplit("Hezekiah Stout", " ").randomAge(),
				User.ofSplit("Roberto Evans", " ").randomAge(),
				User.ofSplit("Braxton Watts", " ").randomAge(),
				User.ofSplit("Jayvon Wilkinson", " ").randomAge(),
				User.ofSplit("Anabelle Chang", " ").randomAge(),
				User.ofSplit("Abigayle Christensen", " ").randomAge(),
				User.ofSplit("Fletcher May", " ").randomAge(),
				User.ofSplit("Marisol Morris", " ").randomAge(),
				User.ofSplit("Grant Wilson", " ").randomAge(),
				User.ofSplit("Hayden Baldwin", " ").randomAge(),
				User.ofSplit("Markus Davidson", " ").randomAge(),
				User.ofSplit("Madelyn Farmer", " ").randomAge(),
				User.ofSplit("Deandre Crosby", " ").randomAge(),
				User.ofSplit("Casey Hardy", " ").randomAge(),
				User.ofSplit("Carmelo Velazquez", " ").randomAge(),
				User.ofSplit("Phillip Hays", " ").randomAge(),
				User.ofSplit("Damari Mcfarland", " ").randomAge(),
				User.ofSplit("Selina Norton", " ").randomAge(),
				User.ofSplit("Lukas Vaughan", " ").randomAge(),
				User.ofSplit("Charlie Carney", " ").randomAge()
		);
	}
}
