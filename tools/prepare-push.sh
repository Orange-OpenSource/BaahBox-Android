#!/bin/bash

#    Orange Baah Box
#    Copyright (C) 2017 – 2019 Orange SA
#
#    This program is free software: you can redistribute it and/or modify
#    it under the terms of the GNU General Public License as published by
#    the Free Software Foundation, either version 3 of the License, or
#    (at your option) any later version.
#
#    This program is distributed in the hope that it will be useful,
#    but WITHOUT ANY WARRANTY; without even the implied warranty of
#    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
#    GNU General Public License for more details.
#
#    You should have received a copy of the GNU General Public License
#    along with this program. If not, see <http://www.gnu.org/licenses/>.

#
# Script which will trigger other scripts to as to prepare the project and its sources for push to the repo or release
# You ned to have an installed JDK so as to run Gradle through command line.
#
# Will do the following actions:
#	- Increment the verison number
#	- Build Dokka-vased documentation using Gradle
#	- Update changelog using Gradle
#

#. *******************************************
# Check the configuration elements for the app
#. *******************************************

./check-appconfiguration.sh

# *********************
# Increase version code
# *********************

./update-versioncode.sh

# *******************
# Build documentation
# *******************

./update-documentation.sh

# ****************
# Update changelog
# ****************

./update-changelog.sh



