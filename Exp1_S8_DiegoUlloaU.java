package teatromoro;

import java.util.Scanner;

public class Exp1_S8_DiegoUlloaU {

    private static final int MAX_VENTAS = 100;

    // Arreglos para almacenar las ventas
    private static final int[]    ventaIds       = new int[MAX_VENTAS];
    private static final int[]    clienteIds     = new int[MAX_VENTAS];
    private static final String[] asientoCodigos = new String[MAX_VENTAS];
    private static final String[] ubicaciones    = new String[MAX_VENTAS];
    private static final double[] descuentos     = new double[MAX_VENTAS];
    private static final double[] preciosFinal   = new double[MAX_VENTAS];

    // Contadores y estadísticas
    private static int totalVentas        = 0;
    private static double totalIngresos   = 0.0;
    private static double totalDescuentos = 0.0;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int opcion;
        do {
            mostrarMenu();
            opcion = leerEntero(sc, "Opción inválida. Ingrese 0–3: ");
            switch (opcion) {
                case 1 -> realizarVenta(sc);
                case 2 -> mostrarResumen();
                case 3 -> generarBoleta(sc);
                case 0 -> System.out.println("¡Hasta luego, vuelva pronto!");
                default -> System.out.println("Debe elegir entre 0 y 3.");
            }
        } while (opcion != 0);
        sc.close();
    }

    private static void mostrarMenu() {
        System.out.println("\n--- Teatro Moro ---");
        System.out.println("1. Venta de entradas");
        System.out.println("2. Resumen de ventas");
        System.out.println("3. Generar boleta");
        System.out.println("0. Salir");
        System.out.print("Elija una opción: ");
    }

    private static void realizarVenta(Scanner sc) {
        if (totalVentas >= MAX_VENTAS) {
            System.out.println("Se han vendido todas las entradas.");
            return;
        }

        // 1) Leer y validar RUT (solo dígitos)
        int idCliente;
        while (true) {
            System.out.print("Ingrese número de RUT (solo dígitos): ");
            String input = sc.nextLine().trim();
            if (input.matches("\\d+")) {
                idCliente = Integer.parseInt(input);
                break;
            } else {
                System.out.println("Error: el RUT solo debe contener dígitos. Intente de nuevo.");
            }
        }

        // 2) Preguntar asiento a reservar
        System.out.print("Código de asiento (ej. A13, B15): ");
        String codigo = sc.nextLine().toUpperCase();
        for (int i = 0; i < totalVentas; i++) {
            if (asientoCodigos[i].equals(codigo)) {
                System.out.println("Asiento " + codigo + " ya está ocupado. Venta cancelada.");
                return;
            }
        }

        // 3) Validar ubicación
        String ubic;
        do {
            System.out.print("Ubicación deseada? (VIP/Platea/Balcón): ");
            ubic = sc.nextLine().trim();
            if (!ubic.equalsIgnoreCase("vip") &&
                    !ubic.equalsIgnoreCase("platea") &&
                    !ubic.equalsIgnoreCase("balcón") &&
                    !ubic.equalsIgnoreCase("balcon")) {
                System.out.println("Ubicación inválida. Debe ser VIP, Platea o Balcón.");
            }
        } while (!ubic.equalsIgnoreCase("vip") &&
                !ubic.equalsIgnoreCase("platea") &&
                !ubic.equalsIgnoreCase("balcón") &&
                !ubic.equalsIgnoreCase("balcon"));

        // Determinar precio base
        double precioBase = switch (ubic.toLowerCase()) {
            case "vip"    -> 30000.0;
            case "platea" -> 25000.0;
            default       -> 20000.0;
        };

        // 4) Descuentos
        System.out.print("Tipo cliente: (e) Estudiante, (t) Tercera edad, (n) Ninguno: ");
        String tipo = sc.nextLine().toLowerCase();
        double dto = 0.0;
        if (tipo.equals("e")) {
            dto = precioBase * 0.10;
        } else if (tipo.equals("t")) {
            dto = precioBase * 0.15;
        }

        double precioFinal = precioBase - dto;

        // 5) Registro de venta
        int idVenta = totalVentas + 1;
        ventaIds[totalVentas]       = idVenta;
        clienteIds[totalVentas]     = idCliente;
        asientoCodigos[totalVentas] = codigo;
        ubicaciones[totalVentas]    = capitalize(ubic);
        descuentos[totalVentas]     = dto;
        preciosFinal[totalVentas]   = precioFinal;

        totalVentas++;
        totalIngresos   += precioFinal;
        totalDescuentos += dto;

        System.out.printf(
                "Venta #%d: Asiento %s (%s), Precio final $%.2f (dto $%.2f)%n",
                idVenta, codigo, capitalize(ubic), precioFinal, dto
        );
    }

    private static void mostrarResumen() {
        if (totalVentas == 0) {
            System.out.println("No hay ventas registradas.");
            return;
        }
        System.out.println("\n--- Resumen de Ventas ---");
        System.out.printf("%-6s %-8s %-8s %-8s %-10s %-10s%n",
                "Venta","Cliente","Asiento","Ubic","Descuento","Precio");
        for (int i = 0; i < totalVentas; i++) {
            System.out.printf("%-6d %-8d %-8s %-8s $%-9.2f $%-9.2f%n",
                    ventaIds[i],
                    clienteIds[i],
                    asientoCodigos[i],
                    ubicaciones[i],
                    descuentos[i],
                    preciosFinal[i]);
        }
        System.out.printf("%nTotal ingresos: $%.2f%n", totalIngresos);
        System.out.printf("Total descuentos: $%.2f%n", totalDescuentos);
    }

    private static void generarBoleta(Scanner sc) {
        System.out.print("Ingrese número de venta para boleta: ");
        int idVenta = leerEntero(sc, "Formato inválido. Ingrese número de venta: ");
        int idx = -1;
        for (int i = 0; i < totalVentas; i++) {
            if (ventaIds[i] == idVenta) { idx = i; break; }
        }
        if (idx < 0) {
            System.out.println("Venta no encontrada.");
            return;
        }
        System.out.println("\n=== BOLETA DE VENTA ===");
        System.out.println("Teatro Moro");
        System.out.println("Venta Nº:   " + ventaIds[idx]);
        System.out.println("Cliente ID: " + clienteIds[idx]);
        System.out.println("Asiento:    " + asientoCodigos[idx]);
        System.out.println("Ubicación:  " + ubicaciones[idx]);
        System.out.printf("Descuento:  $%.2f%n", descuentos[idx]);
        System.out.printf("Precio final: $%.2f%n", preciosFinal[idx]);
        System.out.println("¡Gracias por tu compra!");
    }

    private static int leerEntero(Scanner sc, String msgError) {
        while (true) {
            String linea = sc.nextLine().trim();
            try {
                return Integer.parseInt(linea);
            } catch (NumberFormatException e) {
                System.out.print(msgError);
            }
        }
    }

    private static String capitalize(String s) {
        if (s == null || s.isEmpty()) return s;
        String low = s.toLowerCase();
        return Character.toUpperCase(low.charAt(0)) + low.substring(1);
    }
}
