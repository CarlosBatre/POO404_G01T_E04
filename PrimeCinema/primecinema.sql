-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1:3306
-- Tiempo de generación: 11-09-2024 a las 03:03:52
-- Versión del servidor: 8.3.0
-- Versión de PHP: 8.2.18

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `primecinema`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `entrada`
--

DROP TABLE IF EXISTS `entrada`;
CREATE TABLE IF NOT EXISTS `entrada` (
  `idEntrada` int NOT NULL AUTO_INCREMENT,
  `idUsuario` int NOT NULL,
  `idHorario` int NOT NULL,
  `numeroButaca` int NOT NULL,
  `Formato` varchar(50) NOT NULL,
  `valorFuncion` decimal(10,0) NOT NULL,
  PRIMARY KEY (`idEntrada`),
  KEY `idUsuario` (`idUsuario`),
  KEY `idHorario` (`idHorario`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `horario`
--

DROP TABLE IF EXISTS `horario`;
CREATE TABLE IF NOT EXISTS `horario` (
  `idHorario` int NOT NULL AUTO_INCREMENT,
  `idSala` int NOT NULL,
  `idPelicula` int NOT NULL,
  `horaInicio` time NOT NULL,
  `horaFin` time NOT NULL,
  PRIMARY KEY (`idHorario`),
  KEY `idSala` (`idSala`),
  KEY `idPelicula` (`idPelicula`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `pelicula`
--

DROP TABLE IF EXISTS `pelicula`;
CREATE TABLE IF NOT EXISTS `pelicula` (
  `idPelicula` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(50) NOT NULL,
  `genero` varchar(50) NOT NULL,
  `clasificacion` varchar(50) NOT NULL,
  `formato` varchar(20) NOT NULL,
  `valorFuncionTerceraEdad` decimal(10,2) NOT NULL,
  `valorFuncionAdulto` decimal(10,2) NOT NULL,
  PRIMARY KEY (`idPelicula`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `sala`
--

DROP TABLE IF EXISTS `sala`;
CREATE TABLE IF NOT EXISTS `sala` (
  `idSala` int NOT NULL AUTO_INCREMENT,
  `numeroSala` int NOT NULL,
  `idSucursal` int NOT NULL,
  PRIMARY KEY (`idSala`),
  KEY `idSucursal` (`idSucursal`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `sucursal`
--

DROP TABLE IF EXISTS `sucursal`;
CREATE TABLE IF NOT EXISTS `sucursal` (
  `idSucursal` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(50) NOT NULL,
  `nombreGerente` varchar(20) NOT NULL,
  `numeroTelefono` varchar(15) NOT NULL,
  `direccionCompleta` varchar(100) NOT NULL,
  PRIMARY KEY (`idSucursal`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuario`
--

DROP TABLE IF EXISTS `usuario`;
CREATE TABLE IF NOT EXISTS `usuario` (
  `idUsuario` int NOT NULL AUTO_INCREMENT,
  `NombreCompleto` varchar(65) NOT NULL,
  `login` varchar(50) NOT NULL,
  `contraseña` varchar(50) NOT NULL,
  `numeroDui` varchar(10) NOT NULL,
  `numeroTelefono` varchar(15) NOT NULL,
  `correoElectronico` varchar(80) NOT NULL,
  `direccionCompleta` varchar(120) NOT NULL,
  PRIMARY KEY (`idUsuario`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
