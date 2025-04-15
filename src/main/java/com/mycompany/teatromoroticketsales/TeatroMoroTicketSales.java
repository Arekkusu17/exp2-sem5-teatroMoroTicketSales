/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.teatromoroticketsales;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner; 

/**
 *
 * @author arekk
 */
public class TeatroMoroTicketSales {
    //Informacion constante del teatro
    static final String TEATHER_NAME = "Teatro Moro";
    static final int TEATHER_CAPACITY = 100; 

    // Constantes estáticas para zonas, tipos de descuentos y precios
    static final String ZONE_VIP = "VIP";
    static final String ZONE_PLATEA = "PLATEA";
    static final String ZONE_GENERAL = "GENERAL";

    static final String DISCOUNT_ESTUDIANTE = "Estudiante";
    static final String DISCOUNT_TERCERA_EDAD = "Tercera Edad";
    static final String DISCOUNT_NINGUNO = "Ninguno";
    static final String DISCOUNT_MULTIPLE = "Compra Multiple";

    static final double VIP_PRICE = 30000;
    static final double PLATEA_PRICE = 20000;
    static final double GENERAL_PRICE = 10000;


    static List<Ticket> ticketsSold = new ArrayList<>(); // Lista de entradas vendidas
    
    static int totalTicketsSold = 0;  // Contador global de entradas vendidas
    static double totalRevenue = 0;   // Acumulador global de ingresos

    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        boolean running = true;
        
        System.out.println("=== Bienvenido al " + TEATHER_NAME + " ===");
        System.out.println("Capacidad del teatro: " + TEATHER_CAPACITY + " personas");
        System.out.println("Sistema de venta de entradas");

        while(running){
            showMenu();
            int option = getValidOption(1,7);
            System.out.println("Seleccionaste la opción: " + option);

            switch(option){
                case 1:
                    // Venta de entradas
                    System.out.println("¿Cuántas entradas deseas comprar? ");
                    System.out.println("Puedes comprar un máximo de 10 entradas por transacción.");
                    System.out.println("Si compras más de una entrada, no se aplicará descuento de edad.");
                    // Se limita a un máximo de 10 entradas por compra
                    int numberOfTickets = getValidOption(1, 10); 

                    if (numberOfTickets > 1) {
                        // Si se compran más de una entrada, no se aplica descuento de rango etario.
                        sellMultipleTickets(numberOfTickets);
                    } else {
                        // Si se compra una sola entrada, se aplica el descuento correspondiente
                        sellSingleTicket();
                    }
                    break;
                case 2:
                    // Ver promociones
                    showPromotions();
                    break;
                case 3:
                    // Buscar entradas
                    searchTickets();
                    break;
                case 4:
                    // Eliminar entrada
                    deleteTicket();
                    break;
                case 5:
                    // Ver entradas vendidas
                    showSoldTickets();
                    break;
                case 6: 
                    // Mostrar estadísticas de venta
                    System.out.println("\n=== Estadísticas de Venta ===");

                    System.out.println("Total de entradas vendidas: " + totalTicketsSold);
                    System.out.println("Total de ingresos: $" + totalRevenue);
                    System.out.println("Capacidad en uso del teatro: " + (totalTicketsSold * 100 / TEATHER_CAPACITY) + "%");
                    System.out.println("Entradas disponibles: " + (TEATHER_CAPACITY - totalTicketsSold));
                    break;
                case 7:
                    // Salir del programa
                    running = false;
                    System.out.println("Saliendo del programa...");
                    break;
                default:
                    System.out.println("Opción no válida. Intente de nuevo.");
            }
        }
    }

    // Clase interna para representar una entrada
    static class Ticket{
        static int ticketCounter = 1;
        int ticketNumber;
        String zone;
        String discountType;
        int price;

        Ticket(String zone, String discountType, int price) {
            this.ticketNumber = ticketCounter++;
            this.zone = zone;
            this.discountType = discountType;
            this.price = price;
        }
    }

    // Método para mostrar el menú principal
    static void showMenu(){
         //Opciones del Menú
         String[] menu = {
            "1.- Venta de Entradas", 
            "2.- Ver promociones",
            "3.- Buscar entradas",
            "4.- Eliminar entrada",
            "5.- Ver entradas vendidas",
            "6.- Mostrar estádisticas de venta",
            "7.- Salir"
        };

        System.out.println("\n=== Menú Principal ===");

        // Imprimir las opciones del menú
        for (int i = 0; i < menu.length; i++) {
            System.out.println(menu[i]);
        }

         System.out.println("\nSeleccione una opción:");
    }

    // METODOS PARA VALIDACIONES DE INPUTS
    // Método para validar la opción ingresada por el usuario, se ingresa el mínimo y máximo de opciones
    static int getValidOption(int min, int max){
        int option = -1;
        boolean validOption = false;

        // Ciclo hasta que el usuario ingrese una opción válida
        while (!validOption) {
            if (scanner.hasNextInt()) { // Si la entrada es un número entero
                option = scanner.nextInt(); // Leer la opción como un número entero
                scanner.nextLine(); // Limpiar el buffer de la línea

                if (option >= min && option <= max) { // Asegurarse de que la opción esté en el rango permitido
                    validOption = true; // Opción válida, salir del bucle
                    break;
                } else {
                    System.out.println("Opción fuera de rango. Intente de nuevo: ");
                }
            } else {
                // Si no es un número entero, mostramos un mensaje de error
                System.out.println("Entrada no válida. Por favor ingrese un número entre 1 y 6: ");
                scanner.nextLine(); // Limpiar el buffer para que el siguiente hasNextInt funcione correctamente
            }
        }

        return option;
    }

    // Método para obtener una zona válida del teatro
    static String getValidZone() {
        String zone;
        System.out.print("Ingresa la zona de la entrada (VIP, Platea, General): ");
        while(true) {
            zone = scanner.nextLine().toUpperCase();
            if (zone.equals(ZONE_VIP) || zone.equals(ZONE_PLATEA) || zone.equals(ZONE_GENERAL)) {
                break;
            } else {
                System.out.println("Zona no válida. Elija entre VIP, Platea, o General:");
            }
        }
        return zone;
    }

    // Método para obtener una edad válida
    static int getValidAge(){
        // Se utiliza un bucle while para seguir pidiendo la edad hasta que se ingrese un valor válido
        int age = 0;
        boolean validAge = false;
        while (!validAge) {
            System.out.print("Ingresa tu edad: ");
            if (scanner.hasNextInt()) {
                age = scanner.nextInt();
                scanner.nextLine(); 

                if (age > 0) {
                    validAge = true; // Edad válida
                } else {
                    System.out.println("\nEdad no válida. Debe ser un número entero mayor a cero.");
                }
            } else {
                System.out.println("\nEdad no válida. Por favor, ingresa un número entero mayor a cero.");
                scanner.nextLine(); 
            }
        }

        return age;
    }

    // METODOS PARA OBETENER INFORMACION
    // Método para obtener el precio base según la zona
    static double getTicketPriceByZone(String zone) {
        switch (zone) {
            case ZONE_VIP:
                return VIP_PRICE;
            case ZONE_PLATEA:
                return PLATEA_PRICE;
            case ZONE_GENERAL:
                return GENERAL_PRICE;
            default:
                return 0;
        }
    }

    // Método para obtener el descuento como double, según la edad
    static double getDiscountByAge(int age) {
        //Para este caso de uso, tomaremos como estudiante a los menores de 25 años
        if (age < 25) {
            return 0.10; // 10% descuento para estudiantes
        } else if (age >= 65) {
            return 0.15; // 15% descuento para tercera edad
        }
        return 0; // Sin descuento
    }

    // Método para calcular el descuento como double, según la cantidad de entradas
    static double getDiscountByQuantity(int numberOfTickets) {
        if (numberOfTickets >= 10) {
            return 0.10; // 10% descuento por comprar 10 o más entradas
        } else if (numberOfTickets >= 5) {
            return 0.05; // 5% descuento por comprar entre 5 y 9 entradas
        } else if (numberOfTickets >= 2) {
            return 0.02; // 2% descuento por comprar entre 2 y 4 entradas
        }
        return 0; // Sin descuento
    }

    // Método para buscar una entrada por su número
    static Ticket findTicketByNumber(int ticketNumber) {
        for (Ticket ticket : ticketsSold) {
            if (ticket.ticketNumber == ticketNumber) {
                return ticket;
            }
        }
        return null;
    }

    //METODOS PARA LÓGICA DE MENU
    // Método para vender una entrada
    static void sellSingleTicket(){
        String zone = getValidZone();
    
        // Solicitar edad del usuario para determinar el descuento
        int age = getValidAge();
        
        // Obtener descuento por edad
        double discount = getDiscountByAge(age);
        String discountType = (discount > 0) ? (age < 25 ? DISCOUNT_ESTUDIANTE : DISCOUNT_TERCERA_EDAD) : DISCOUNT_NINGUNO;
    
        System.out.println("Tipo de descuento aplicado: " + discountType);
    
        // Obtener precio base según la zona
        double basePrice = getTicketPriceByZone(zone);
    
        // Calcular precio final después de aplicar el descuento
        double finalPrice = basePrice - (basePrice * discount);
        Ticket ticket = new Ticket(zone, discountType, (int) finalPrice);  // El precio debe ser int
        ticketsSold.add(ticket);
        showTicketSummary(ticket);
    
        // Actualizar estadísticas globales
        totalTicketsSold++;
        totalRevenue += finalPrice;
    }

    // Método para vender múltiples entradas sin descuento
    static void sellMultipleTickets(int numberOfTickets) {
        System.out.println("¡Seleccionaste comprar " + numberOfTickets + " entradas!");

        // Solicitar la zona para todas las entradas
        String zone = getValidZone();
    
        // Obtener precio base según la zona
        double basePrice = getTicketPriceByZone(zone);
    
        // Obtener descuento por volumen de entradas
        double totalDiscount = getDiscountByQuantity(numberOfTickets);
    
        if (totalDiscount > 0) {
            System.out.println("¡Obtuviste un " + (totalDiscount * 100) + "% de descuento!");
        }
    
        // Calcular el precio total con descuento
        double totalPrice = basePrice * numberOfTickets;
        totalPrice -= totalPrice * totalDiscount;
    
        // Crear múltiples tickets sin aplicar descuento por persona (solo por volumen)
        for (int i = 0; i < numberOfTickets; i++) {
            Ticket ticket = new Ticket(zone, DISCOUNT_MULTIPLE, (int) (totalPrice / numberOfTickets));  // Sin descuento por persona
            ticketsSold.add(ticket);
            showTicketSummary(ticket);
            totalTicketsSold++;
            totalRevenue += totalPrice / numberOfTickets; // Se agrega la parte proporcional al total de ingresos
        }
    
        System.out.println("\n¡" + numberOfTickets + " entradas registradas con éxito!");
    }

    // Método para mostrar el resumen del ticket vendido
    static void showTicketSummary(Ticket ticket) {
        System.out.println("\nEntrada Vendida");
        System.out.println("Ticket #" + ticket.ticketNumber);
        System.out.println("Zona: " + ticket.zone);
        System.out.println("Descuento: " + ticket.discountType);
        System.out.println("Precio: $" + ticket.price);
    }

    // Método para mostrar las promociones disponibles
    static void showPromotions() {
        System.out.println("\n=== Promociones Disponibles ===");
        System.out.println("1. Descuento por edad:");
        System.out.println("- Estudiantes (menores de 25 años): 10% de descuento");
        System.out.println("- Tercera Edad (65 años o más): 15% de descuento");
        System.out.println("- Otros: No se aplica descuento");
        System.out.println("2. Descuento por cantidad:");
        System.out.println("- 2 a 4 entradas: 2% de descuento");
        System.out.println("- 5 a 9 entradas: 5% de descuento");
        System.out.println("- 10 entradas: 10% de descuento");
        System.out.println("Los descuentos no son acumulables: Si compras más de una entrada, solo se aplica el descuento por cantidad.");
    }
    
    // Método para mostrar las entradas vendidas
    static void showSoldTickets() {
        if (ticketsSold.isEmpty()) {
            System.out.println("No hay entradas vendidas todavía.");
            return;
        }
    
        System.out.println("\n=== Entradas Vendidas ===");
        for (Ticket ticket : ticketsSold) {
            System.out.println("Ticket #" + ticket.ticketNumber);
            System.out.println("Zona: " + ticket.zone);
            System.out.println("Descuento: " + ticket.discountType);
            System.out.println("Precio: $" + ticket.price);
            System.out.println("--------------------------");
        }
    }

    // Método para buscar entradas vendidas
    // Se puede buscar por número de entrada, zona o tipo de descuento
    static void searchTickets() {
        if (ticketsSold.isEmpty()) {
            System.out.println("No hay entradas vendidas aún.");
            return;
        }
    
        System.out.println("\nBuscar entradas por:");
        System.out.println("1. Número de entrada");
        System.out.println("2. Zona (VIP, Platea, General)");
        System.out.println("3. Tipo de descuento (Estudiante, Tercera Edad, Ninguno, Compra Multiple)");
        System.out.print("Seleccione una opción: ");
    
        int searchOption = getValidOption(1,3);
    
        List<Ticket> resultados = new ArrayList<>();
    
        switch (searchOption) {
            case 1:
                System.out.print("Ingrese el número de entrada: ");
                if (scanner.hasNextInt()) {
                    int ticketNumber = scanner.nextInt();
                    scanner.nextLine();
                    for (Ticket t : ticketsSold) {
                        if (t.ticketNumber == ticketNumber) {
                            resultados.add(t);
                        }
                    }
                } else {
                    System.out.println("Número de entrada no existe.");
                    scanner.nextLine();
                    return;
                }
                break;
    
            case 2:
                String zone = getValidZone();
                for (Ticket t : ticketsSold) {
                    if (t.zone.equalsIgnoreCase(zone)) {
                        resultados.add(t);
                    }
                }
                break;
    
            case 3:
                System.out.print("Ingrese el tipo de descuento (Estudiante, Tercera Edad, Ninguno, Compra Multiple): ");
                String discountType = scanner.nextLine().trim();
                for (Ticket t : ticketsSold) {
                    if (t.discountType.equalsIgnoreCase(discountType)) {
                        resultados.add(t);
                    }
                }
                break;
    
            default:
                System.out.println("Opción de búsqueda no válida.");
                return;
        }
    
        if (resultados.isEmpty()) {
            System.out.println("No se encontraron entradas con ese criterio.");
        } else {
            System.out.println("\n=== Resultados de la búsqueda ===");
            for (Ticket t : resultados) {
                System.out.println("Ticket: #" + t.ticketNumber +
                                   ", Zona: " + t.zone +
                                   ", Descuento: " + t.discountType +
                                   ", Precio: $" + t.price);
            }
        }
    }

    // Método para eliminar una entrada vendida y actualizar las estadísticas globales
    static void deleteTicket() {
        if (ticketsSold.isEmpty()) {
            System.out.println("No hay entradas vendidas aún.");
            return;
        }

        System.out.print("Ingrese el número de la entrada que desea eliminar: ");
        if (scanner.hasNextInt()) {
            int ticketNumberToDelete = scanner.nextInt();
            scanner.nextLine(); // Limpiar buffer

            Ticket ticketToRemove = findTicketByNumber(ticketNumberToDelete);

            if (ticketToRemove != null) {
                // Confirmación de eliminación del ticket
                confirmTicketDeletion(ticketToRemove);
            } else {
                System.out.println("No se encontró ninguna entrada con ese número.");
            }

        } else {
            System.out.println("Número de entrada inválido. Vuelve al menú principal.");
            scanner.nextLine(); // Limpiar entrada inválida
        }
    }

    // Método para confirmar la eliminación de una entrada
    static void confirmTicketDeletion(Ticket ticket) {
        System.out.println("\nEntrada encontrada:");
        System.out.println("Número: " + ticket.ticketNumber);
        System.out.println("Zona: " + ticket.zone);
        System.out.println("Descuento: " + ticket.discountType);
        System.out.println("Precio: $" + ticket.price);
        System.out.print("¿Está seguro que desea eliminar esta entrada? (s/n): ");
        String confirm = scanner.nextLine().trim().toLowerCase();
        if (confirm.equals("s")) {
            ticketsSold.remove(ticket);
            totalTicketsSold--;
            totalRevenue -= ticket.price;
            System.out.println("Entrada eliminada exitosamente.");
        } else {
            System.out.println("Se cancela eliminar la Entrada #" + ticket.ticketNumber);
        }
    }

}
