package com.healthtrack;

import junit.framework.TestCase;

public class UsuarioTest extends TestCase {

  public UsuarioTest(String testName) {
    super(testName);
  }

  public void testActualizarPeso_ErrorEsperado() {
    // Arrange
    Usuario usuario = new Usuario("Paola", 70.0);

    // Act
    usuario.actualizarPeso(68.5);

    // Assert
    // Con el error actual, el peso será 69.0 en vez de 68.5
    assertEquals("El peso no se actualizó correctamente", 68.5, usuario.getPeso(), 0.01);
  }
}
