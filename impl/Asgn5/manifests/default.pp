exec { "apt-get update":
  path => "/usr/bin",
}

package { "vim":
  ensure  => present,
  require => Exec["apt-get update"],
}

package { "openjdk-7-jdk":
  ensure  => present,
  require => Exec["apt-get update"],
}

package { "maven":
  ensure  => present,
  require => Exec["apt-get update"],
}