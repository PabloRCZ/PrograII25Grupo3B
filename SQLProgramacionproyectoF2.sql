USE [master];
GO

IF DB_ID('Brayan') IS NOT NULL
    DROP DATABASE Brayan;
GO

CREATE DATABASE Brayan;
GO

USE Brayan;
GO

/* ================================
   TABLAS PRINCIPALES
================================ */

CREATE TABLE Usuario (
    id INT IDENTITY(1,1) PRIMARY KEY,
    nombre NVARCHAR(100) NOT NULL,
    rol NVARCHAR(20) CHECK (rol IN ('administrador','empleado')) NOT NULL,
    email NVARCHAR(100) UNIQUE NOT NULL,
    contraseña NVARCHAR(255) NOT NULL,
    fecha_registro DATETIME DEFAULT GETDATE()
);
GO

CREATE TABLE Cliente (
    id INT IDENTITY(1,1) PRIMARY KEY,
    nombre NVARCHAR(100) NOT NULL,
    telefono NVARCHAR(15),
    direccion NVARCHAR(MAX),
    email NVARCHAR(100),
    fecha_registro DATETIME DEFAULT GETDATE()
);
GO

CREATE TABLE Proveedor (
    id INT IDENTITY(1,1) PRIMARY KEY,
    nombre NVARCHAR(100) NOT NULL,
    telefono NVARCHAR(15),
    direccion NVARCHAR(MAX),
    producto NVARCHAR(100),
    email NVARCHAR(100)
);
GO

CREATE TABLE Factura (
    id INT IDENTITY(1,1) PRIMARY KEY,
    numero NVARCHAR(50) UNIQUE NOT NULL,
    fecha DATE NOT NULL,
    tipo NVARCHAR(10) CHECK (tipo IN ('venta','compra')) NOT NULL,
    total DECIMAL(10,2) NOT NULL,
    estado NVARCHAR(20) CHECK (estado IN ('pendiente','pagada')) DEFAULT 'pendiente',
    created_at DATETIME DEFAULT GETDATE()
);
GO

CREATE TABLE Venta (
    id INT IDENTITY(1,1) PRIMARY KEY,
    fecha DATE NOT NULL,
    cliente_id INT NOT NULL,
    usuario_id INT NOT NULL,
    total DECIMAL(10,2) NOT NULL,
    metodo_pago NVARCHAR(20) CHECK (metodo_pago IN ('efectivo','tarjeta','transferencia','crédito')) DEFAULT 'efectivo',
    factura_id INT UNIQUE NULL,
    created_at DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (cliente_id) REFERENCES Cliente(id) ON DELETE CASCADE,
    FOREIGN KEY (usuario_id) REFERENCES Usuario(id) ON DELETE CASCADE,
    FOREIGN KEY (factura_id) REFERENCES Factura(id) ON DELETE SET NULL
);
GO

CREATE TABLE Compra (
    id INT IDENTITY(1,1) PRIMARY KEY,
    fecha DATE NOT NULL,
    proveedor_id INT NOT NULL,
    usuario_id INT NOT NULL,
    total DECIMAL(10,2) NOT NULL,
    factura_id INT UNIQUE NULL,
    created_at DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (proveedor_id) REFERENCES Proveedor(id) ON DELETE CASCADE,
    FOREIGN KEY (usuario_id) REFERENCES Usuario(id) ON DELETE CASCADE,
    FOREIGN KEY (factura_id) REFERENCES Factura(id) ON DELETE SET NULL
);
GO

CREATE TABLE Inventario (
    id INT IDENTITY(1,1) PRIMARY KEY,
    nombre NVARCHAR(100) NOT NULL,
    descripcion NVARCHAR(MAX),
    cantidad INT DEFAULT 0,
    unidad NVARCHAR(20) NOT NULL,
    precio_costo DECIMAL(10,2) NOT NULL,
    precio_venta DECIMAL(10,2) NOT NULL,
    fecha_vencimiento DATE,
    minimo_stock INT DEFAULT 5,
    created_at DATETIME DEFAULT GETDATE(),
    updated_at DATETIME DEFAULT GETDATE()
);
GO

CREATE TABLE InventarioAnimales (
    id INT IDENTITY(1,1) PRIMARY KEY,
    especie NVARCHAR(50) NOT NULL,
    raza NVARCHAR(50),
    peso DECIMAL(6,2),
    edad INT,
    estado NVARCHAR(20) CHECK (estado IN ('activo','vendido','muerto','enfermo')) DEFAULT 'activo',
    fecha_ingreso DATE NOT NULL,
    identificador_unico NVARCHAR(50) UNIQUE NOT NULL,
    observaciones NVARCHAR(MAX),
    created_at DATETIME DEFAULT GETDATE(),
    updated_at DATETIME DEFAULT GETDATE()
);
GO

CREATE TABLE DetalleVenta (
    id INT IDENTITY(1,1) PRIMARY KEY,
    venta_id INT NOT NULL,
    producto_id INT NULL,
    animal_id INT NULL,
    cantidad INT DEFAULT 1,
    precio_unitario DECIMAL(10,2) NOT NULL,
    subtotal DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (venta_id) REFERENCES Venta(id) ON DELETE CASCADE,
    FOREIGN KEY (producto_id) REFERENCES Inventario(id) ON DELETE SET NULL,
    FOREIGN KEY (animal_id) REFERENCES InventarioAnimales(id) ON DELETE SET NULL,
    CHECK (producto_id IS NOT NULL OR animal_id IS NOT NULL)
);
GO

CREATE TABLE DetalleCompra (
    id INT IDENTITY(1,1) PRIMARY KEY,
    compra_id INT NOT NULL,
    producto_id INT NULL,
    animal_id INT NULL,
    cantidad INT NOT NULL,
    precio_unitario DECIMAL(10,2) NOT NULL,
    subtotal DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (compra_id) REFERENCES Compra(id) ON DELETE CASCADE,
    FOREIGN KEY (producto_id) REFERENCES Inventario(id) ON DELETE SET NULL,
    FOREIGN KEY (animal_id) REFERENCES InventarioAnimales(id) ON DELETE SET NULL,
    CHECK (producto_id IS NOT NULL OR animal_id IS NOT NULL)
);
GO

CREATE TABLE EventoAnimal (
    id INT IDENTITY(1,1) PRIMARY KEY,
    animal_id INT NOT NULL,
    tipo NVARCHAR(50) NOT NULL,
    descripcion NVARCHAR(MAX),
    fecha DATE NOT NULL,
    creado_por INT NULL,
    created_at DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (animal_id) REFERENCES InventarioAnimales(id) ON DELETE CASCADE,
    FOREIGN KEY (creado_por) REFERENCES Usuario(id) ON DELETE SET NULL
);
GO

/* ================================
   NUEVA TABLA: Bitácora de Accesos
================================ */

CREATE TABLE BitacoraAcceso (
    id INT IDENTITY(1,1) PRIMARY KEY,
    usuario_id INT NOT NULL,
    fecha_acceso DATETIME DEFAULT GETDATE(),
    accion NVARCHAR(20) CHECK (accion IN ('login','logout')) NOT NULL,
    ip NVARCHAR(50) NULL,
    FOREIGN KEY (usuario_id) REFERENCES Usuario(id)
);
GO

/* ================================
   ÍNDICES DE OPTIMIZACIÓN
================================ */

CREATE INDEX idx_venta_cliente ON Venta(cliente_id);
CREATE INDEX idx_compra_proveedor ON Compra(proveedor_id);
CREATE INDEX idx_detalle_venta_venta ON DetalleVenta(venta_id);
CREATE INDEX idx_detalle_compra_compra ON DetalleCompra(compra_id);
CREATE INDEX idx_inventario_animales_estado ON InventarioAnimales(estado);
CREATE INDEX idx_factura_tipo_fecha ON Factura(tipo, fecha);
CREATE INDEX idx_evento_animal_fecha ON EventoAnimal(fecha);
GO

PRINT '✅ Base de datos brayan creada correctamente con BitácoraAcceso incluida.';

