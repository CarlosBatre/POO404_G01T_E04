-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1:3306
-- Tiempo de generación: 15-09-2024 a las 04:43:44
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
  `idSucursal` int NOT NULL,
  `idSala` int NOT NULL,
  `numeroButaca` int NOT NULL,
  `Formato` varchar(50) NOT NULL,
  `valorFuncion` decimal(10,0) NOT NULL,
  PRIMARY KEY (`idEntrada`),
  UNIQUE KEY `idSucursal` (`idSucursal`),
  UNIQUE KEY `idSala` (`idSala`),
  KEY `idUsuario` (`idUsuario`)
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
  `horaProyeccion` time NOT NULL,
  PRIMARY KEY (`idPelicula`)
) ENGINE=MyISAM AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `pelicula`
--

INSERT INTO `pelicula` (`idPelicula`, `nombre`, `genero`, `clasificacion`, `formato`, `valorFuncionTerceraEdad`, `valorFuncionAdulto`, `horaProyeccion`) VALUES
(4, 'Sonic (el increibre erizo sonico)', 'realista, fumada, videojuegos', 'A', 'Tradicional', 3.90, 5.00, '10:45:00'),
(2, 'Transformers: Venganza de los Caidos', 'Accion', 'B', '3D', 5.60, 6.55, '00:00:00'),
(5, 'FNAF', 'No se, furros', 'Para todos..', '3D', 5.60, 6.55, '09:30:00');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `sala`
--

DROP TABLE IF EXISTS `sala`;
CREATE TABLE IF NOT EXISTS `sala` (
  `idSala` int NOT NULL AUTO_INCREMENT,
  `numero` int NOT NULL,
  `idSucursal` int NOT NULL,
  PRIMARY KEY (`idSala`),
  KEY `idSucursal` (`idSucursal`)
) ENGINE=MyISAM AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `sala`
--

INSERT INTO `sala` (`idSala`, `numero`, `idSucursal`) VALUES
(2, 3, 1),
(3, 1, 2),
(4, 6, 8),
(5, 12, 8),
(6, 121, 8),
(7, 1, 8),
(8, 6, 8),
(9, 7, 5),
(10, 1, 5),
(11, 12, 5),
(12, 125, 5),
(13, 125, 5),
(14, 1253, 5),
(15, 46, 1),
(16, 46, 1);

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
  PRIMARY KEY (`idSucursal`),
  UNIQUE KEY `nombre` (`nombre`)
) ENGINE=MyISAM AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `sucursal`
--

INSERT INTO `sucursal` (`idSucursal`, `nombre`, `nombreGerente`, `numeroTelefono`, `direccionCompleta`) VALUES
(1, 'Plaza Mundo Soyapango', 'Josue Manzano', '7777-7777', 'San Salvador, Soyapango, PlazaMundo'),
(2, 'a', 'a', 'a', 'a'),
(3, 'b', 'b', 'b', 'b'),
(4, 'c', 'c', 'c', 'c'),
(5, 'Galerias', 'Fabrizio ', '2290-0000', 'Galerias'),
(6, 'Metrocento San Miguel', 'Raul', '12121212', 'a'),
(7, 'g', 'g', 'g', 'g'),
(8, 'PlazaMundo Apopa', 'Milo', '568430', 'Apopa XD');

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
  PRIMARY KEY (`idUsuario`),
  UNIQUE KEY `numeroDui` (`numeroDui`),
  UNIQUE KEY `login` (`login`)
) ENGINE=MyISAM AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `usuario`
--

INSERT INTO `usuario` (`idUsuario`, `NombreCompleto`, `login`, `contraseña`, `numeroDui`, `numeroTelefono`, `correoElectronico`, `direccionCompleta`) VALUES
(1, 'Carlos Josue Ruano Batres', 'Kayn', '1234qwer', '2328473', '77777777', 'kayn@hotmail.com', 'San Salvador, Soyapango, Universidad Don Bosco'),
(2, 'a', 'a', 'a', 'a', 'a', 'a', 'a'),
(4, 'Joaquin Xokas', 'Xokas', '12345', '11223344', '77778888', 'experto@gmail.com', 'España, Madrid');
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
