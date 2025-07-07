## 🔍 Análisis del estado actual de la plataforma

La plataforma de salud desarrollada por la empresa **HealthTrack** permite a los usuarios registrar su peso y actualizarlo periódicamente. Sin embargo, actualmente presenta un error crítico en su lógica de negocio: al intentar actualizar el peso, el sistema no guarda el valor ingresado por el usuario, sino que reduce automáticamente 1 kilogramo del peso actual. Este comportamiento erróneo se encuentra en el método actualizarPeso() de la clase Usuario, donde en lugar de asignar el nuevo valor (this.peso = nuevoPeso), se ejecuta una resta fija (this.peso -= 1), lo que altera el registro real del usuario sin ningún control.

El impacto de este error en la experiencia del usuario es significativo. Al tratarse de una plataforma enfocada en el seguimiento de la salud personal, cualquier inconsistencia en los datos puede generar desconfianza en el sistema y afectar negativamente la percepción de su utilidad. Los usuarios que intentan llevar un registro riguroso de su evolución se ven perjudicados por esta falla, ya que el sistema no refleja sus entradas reales, afectando la confiabilidad del servicio.

Además, la ausencia de pruebas automatizadas en el desarrollo actual ha permitido que este error pase desapercibido hasta llegar a producción. No se han implementado pruebas unitarias para validar el comportamiento básico de los métodos, ni pruebas funcionales que simulen el flujo completo del usuario. Tampoco existen pruebas de regresión que alerten sobre errores introducidos en versiones posteriores ni pruebas de rendimiento para medir el comportamiento bajo carga. Esta falta de validación automática, sumada a la inexistencia de un pipeline de CI/CD, expone a la plataforma a fallos recurrentes, regresiones y pérdida de calidad en el producto final.

---------

## 🧪 Diseño y Desarrollo de Pruebas Automatizadas

Con el objetivo de abordar el error identificado y mejorar la calidad general del sistema, se diseñó una estrategia integral de pruebas automatizadas que abarca distintos niveles: pruebas unitarias, funcionales, de regresión y de rendimiento. Cada una de estas pruebas tiene un rol específico en la detección temprana de fallos, la validación del comportamiento esperado del sistema y la prevención de errores futuros.

En primer lugar, se implementaron **pruebas unitarias** utilizando JUnit 3 para verificar de forma aislada el comportamiento del método `actualizarPeso()`. La prueba creada simula la creación de un usuario con un peso inicial y evalúa si, al actualizar su peso, el sistema guarda correctamente el nuevo valor. Al ejecutarse sobre la versión del código con el error presente, la prueba falló, confirmando el comportamiento incorrecto del sistema y validando la utilidad del test automatizado, esto se puede verificar en el workflow 2 "ci: corregir configuración de Java en GitHub Actions agregando distribución 'temurin'". Al corregir el error de la asignación podemos ver el estado del nuevo test en el workflow 3 "fix: corregir lógica en actualizarPeso() para asignar el nuevo valor ingresado". Dada la falta de un mecanismo de guardado del registro del peso, se omite la validación de actualización de 48 horas.

Posteriormente, se implementó **pruebas funcionales** utilizando Selenium WebDriver. Esta prueba está orientada a simular el flujo del usuario dentro de la plataforma: desde la actualización del peso y la validación visual del nuevo valor en pantalla. La prueba *testActualizarPesoDesdeFormulario* automatiza el flujo completo de ingreso de un nuevo peso y validación de la respuesta en pantalla. Se ejecuta en un entorno Docker compuesto por contenedores para el frontend (html-server) y Selenium (selenium/standalone-chrome). La prueba usa RemoteWebDriver, espera elementos dinámicos con WebDriverWait y valida el contenido del DOM actualizado.

Para cubrir escenarios futuros, se estableció una estrategia de **pruebas de regresión** basada en la integración continua. Cada cambio realizado sobre el código relacionado con la lógica del peso o los datos del usuario debe gatillar automáticamente la ejecución de las pruebas unitarias. Esto permitirá detectar de inmediato cualquier error introducido por nuevas modificaciones, reduciendo el riesgo de fallos en versiones posteriores.

Finalmente, se propuso la incorporación de **pruebas de rendimiento** mediante la herramienta JMeter. Estas pruebas tienen como objetivo evaluar el comportamiento del sistema ante múltiples solicitudes concurrentes, simulando a decenas de usuarios interactuando con la plataforma al mismo tiempo. El resultado de estas pruebas se dejan dentro de los artefactos de GitHub Actions, en los resultados de ejecución del pipeline correspondiente (chore: Integración de prueba de carga con JMeter en CI usando Docker … #17)

En conjunto, esta estrategia de pruebas no solo permite validar la corrección del sistema actual, sino que establece las bases para un desarrollo más confiable y sostenible a futuro.


---------

## 🔄 Automatización del Proceso de Pruebas con CI/CD (2 pts)

Como parte de la estrategia de mejora continua en la plataforma HealthTrack, se configuró un pipeline de integración continua utilizando **GitHub Actions**, con el objetivo de automatizar la validación del código y garantizar que los errores sean detectados de forma temprana. Esta automatización permite ejecutar las pruebas de manera inmediata tras cada cambio en el repositorio, reduciendo el tiempo entre el desarrollo y la identificación de fallos.

El pipeline está definido dentro de un archivo YAML en el directorio `.github/workflows/` y se activa automáticamente ante cada `push` a la rama `main`. Su configuración incluye los pasos necesarios para clonar el repositorio, configurar el entorno de Java, compilar el proyecto con Maven y ejecutar las pruebas unitarias definidas. De esta forma, cualquier desarrollador que trabaje sobre el código puede obtener retroalimentación inmediata sobre el impacto de sus cambios.

El pipeline constituye un primer paso hacia una cultura DevOps en la organización, promoviendo la automatización, la trazabilidad y la confianza en cada entrega de software. A medida que se integren nuevas pruebas funcionales y de rendimiento, este flujo de automatización se expandirá para ofrecer una validación completa del sistema antes de cada despliegue, mejorando significativamente la calidad del producto final.
