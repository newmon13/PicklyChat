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

import io.github.palexdev.materialfx.utils.RandomUtils;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;

@Slf4j
@Getter
public class User {
	private String username;
	private String name;
	private String surname;
    @Setter
    private byte[] avatar;
	private Status status;
	private ObservableList<String> observableAttributes;
	private String clientID;

	@Setter
    private int age;

	public User() {
		observableAttributes = FXCollections.observableArrayList();
		observableAttributes.addListener((ListChangeListener<String>) change -> {
			while (change.next()){
				if (change.wasAdded()) {
					log.info("User: {} updated data", username );
				} else if (change.wasRemoved()) {
					log.info("User: {} removed data", username );

				} else if (change.wasUpdated()) {
					log.info("User: {} changed data", username );
				} else {
					log.info("User: {} did sth with his data", username );
				}
			}
		});
	}

	public User(String username) {
		this();
        this.username = username;
		observableAttributes.add(username);
	}

	public User(String name, String surname) {
		this();
		this.name = name;
		this.surname = surname;
		observableAttributes.addAll(name, surname);
	}

	public static User ofSplit(String fullName, String split) {
		String[] fNameArray = fullName.split(split);
		return new User(fNameArray[0], fNameArray[1]);
	}

    public User randomAge() {
		setAge(RandomUtils.random.nextInt(18, 81));
		return this;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		User user = (User) o;
		return getName().equals(user.getName());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getName());
	}

}
