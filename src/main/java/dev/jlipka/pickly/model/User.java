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
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
public class User {
	private final String name;
	private final String surname;
	@Setter
    private int age;

	public User(String name) {
		this.name = name;
		this.surname = "";
	}

	public User(String name, String surname) {
		this.name = name;
		this.surname = surname;
	}

	public User(String name, String surname, int age) {
		this.name = name;
		this.surname = surname;
		this.age = age;
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
