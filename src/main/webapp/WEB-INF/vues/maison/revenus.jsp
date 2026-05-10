<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.examen_java.modele.Revenu" %>
<%@ page import="java.util.List" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%
    List<Revenu> revenus = (List<Revenu>) request.getAttribute("revenus");
    double totalGeneral = (Double) request.getAttribute("totalGeneral");
    double totalMois = (Double) request.getAttribute("totalMois");
    double totalFiltre = (Double) request.getAttribute("totalFiltre");
    int nombreTransactions = (Integer) request.getAttribute("nombreTransactions");
    int nombreRevenus = (Integer) request.getAttribute("nombreRevenus");
    List<Object[]> revenuParMaison = (List<Object[]>) request.getAttribute("revenuParMaison");
    String filtreActif = (String) request.getAttribute("filtreActif");
    
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", new java.util.Locale("fr", "FR"));
    SimpleDateFormat dateCourte = new SimpleDateFormat("dd/MM/yyyy");
    SimpleDateFormat moisFormat = new SimpleDateFormat("MMMM yyyy", new java.util.Locale("fr", "FR"));
%>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Revenus - Location Maisons</title>

    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/maison-style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/revenus-style.css">
</head>
<body>

    <jsp:include page="/WEB-INF/vues/commun/header-dashboard.jsp" />

    <main class="maison-main">
        <div class="maison-container">

            
            <div class="revenus-entete">
                <div class="entete-gauche">
                    <a href="${pageContext.request.contextPath}/dashboard" class="retour-lien">
                        <i class="fa-solid fa-arrow-left"></i> Retour au dashboard
                    </a>
                    <h1>
                        <i class="fa-solid fa-chart-line"></i> 
                        Mes revenus
                    </h1>
                    <p>
                        Suivez vos gains et l'historique de vos transactions.
                    </p>
                </div>
            </div>

            
            <div class="revenus-resume">
                <div class="resume-carte resume-total">
                    <div class="resume-icone">
                        <i class="fa-solid fa-euro-sign"></i>
                    </div>
                    <div class="resume-infos">
                        <span class="resume-valeur"><%= String.format("%.0f", totalGeneral) %> €</span>
                        <span class="resume-label">Revenu total</span>
                    </div>
                </div>

                <div class="resume-carte resume-mois">
                    <div class="resume-icone">
                        <i class="fa-solid fa-calendar-check"></i>
                    </div>
                    <div class="resume-infos">
                        <span class="resume-valeur"><%= String.format("%.0f", totalMois) %> €</span>
                        <span class="resume-label">Ce mois (<%= moisFormat.format(new java.util.Date()) %>)</span>
                    </div>
                </div>

                <div class="resume-carte resume-transactions">
                    <div class="resume-icone">
                        <i class="fa-solid fa-receipt"></i>
                    </div>
                    <div class="resume-infos">
                        <span class="resume-valeur"><%= nombreTransactions %></span>
                        <span class="resume-label">Transactions réussies</span>
                    </div>
                </div>

                <div class="resume-carte resume-filtre">
                    <div class="resume-icone">
                        <i class="fa-solid fa-filter"></i>
                    </div>
                    <div class="resume-infos">
                        <span class="resume-valeur"><%= String.format("%.0f", totalFiltre) %> €</span>
                        <span class="resume-label">
                            <% if ("mois".equals(filtreActif)) { %>Ce mois
                            <% } else if ("trimestre".equals(filtreActif)) { %>Ce trimestre
                            <% } else if ("annee".equals(filtreActif)) { %>Cette année
                            <% } else if ("personnalise".equals(filtreActif)) { %>Période sélectionnée
                            <% } else { %>Total affiché<% } %>
                        </span>
                    </div>
                </div>
            </div>

            
            <div class="revenus-filtres">
                <div class="filtres-boutons">
                    <a href="${pageContext.request.contextPath}/revenus" 
                       class="filtre-bouton <%= "tout".equals(filtreActif) ? "filtre-actif" : "" %>">
                        <i class="fa-solid fa-list"></i> Tout
                    </a>
                    <a href="${pageContext.request.contextPath}/revenus?periode=mois" 
                       class="filtre-bouton <%= "mois".equals(filtreActif) ? "filtre-actif" : "" %>">
                        <i class="fa-solid fa-calendar-week"></i> Ce mois
                    </a>
                    <a href="${pageContext.request.contextPath}/revenus?periode=trimestre" 
                       class="filtre-bouton <%= "trimestre".equals(filtreActif) ? "filtre-actif" : "" %>">
                        <i class="fa-solid fa-calendar-days"></i> Ce trimestre
                    </a>
                    <a href="${pageContext.request.contextPath}/revenus?periode=annee" 
                       class="filtre-bouton <%= "annee".equals(filtreActif) ? "filtre-actif" : "" %>">
                        <i class="fa-solid fa-calendar"></i> Cette année
                    </a>
                </div>
            </div>

            
            <div class="revenus-section">
                <div class="section-entete">
                    <h2>
                        <i class="fa-solid fa-list-ul"></i> 
                        Transactions 
                        <span class="resultat-compteur"><%= nombreRevenus %> résultat<%= nombreRevenus > 1 ? "s" : "" %></span>
                    </h2>
                </div>

                <% if (revenus != null && !revenus.isEmpty()) { %>
                    <div class="tableau-container">
                        <table class="tableau-revenus">
                            <thead>
                                <tr>
                                    <th>Date</th>
                                    <th>Maison</th>
                                    <th>Locataire</th>
                                    <th>Séjour</th>
                                    <th>Montant</th>
                                    <th>Statut</th>
                                </tr>
                            </thead>
                            <tbody>
                                <% for (Revenu r : revenus) { %>
                                    <tr>
                                        <td class="col-date">
                                            <span class="date-valeur"><%= dateCourte.format(r.getDatePaiement()) %></span>
                                        </td>
                                        <td class="col-maison">
                                            <div>
                                                <span class="maison-nom"><%= r.getMaisonTitre() %></span>
                                            </div>
                                        </td>
                                        <td class="col-locataire">
                                            <span><%= r.getLocataireNomComplet() %></span>
                                        </td>
                                        <td class="col-sejour">
                                            <span><%= r.getPeriodeSejour() %></span>
                                        </td>
                                        <td class="col-montant">
                                            <span class="montant-valeur"><%= r.getMontantFormate() %></span>
                                        </td>
                                        <td class="col-statut">
                                            <span class="statut-badge <%= r.getStatutClass() %>">
                                                <%= r.getStatutLibelle() %>
                                            </span>
                                        </td>
                                    </tr>
                                <% } %>
                            </tbody>
                        </table>
                    </div>
                <% } else { %>
                    <div class="etat-vide-revenus">
                        <div class="vide-illustration">
                            <i class="fa-solid fa-chart-line"></i>
                        </div>
                        <h3>Aucune transaction trouvée</h3>
                        <p>
                            <% if (!"tout".equals(filtreActif)) { %>
                                Aucune transaction pour cette période. Essayez un autre filtre.
                            <% } else { %>
                                Vous n'avez pas encore de revenus. Les paiements de vos locataires apparaîtront ici.
                            <% } %>
                        </p>
                        <% if (!"tout".equals(filtreActif)) { %>
                            <a href="${pageContext.request.contextPath}/revenus" class="bouton-ajouter">
                                <i class="fa-solid fa-list"></i> Voir toutes les transactions
                            </a>
                        <% } %>
                    </div>
                <% } %>
            </div>

            
            <% if (revenuParMaison != null && !revenuParMaison.isEmpty()) { %>
                <div class="revenus-section">
                    <div class="section-entete">
                        <h2>
                            <i class="fa-solid fa-house-laptop"></i> 
                            Revenu par maison
                        </h2>
                    </div>

                    <div class="revenu-maison-liste">
                        <% for (Object[] ligne : revenuParMaison) { 
                            double total = (Double) ligne[2];
                            double pourcentage = totalGeneral > 0 ? (total / totalGeneral) * 100 : 0;
                        %>
                            <div class="revenu-maison-item">
                                <div class="maison-info">
                                    <span class="maison-titre"><%= ligne[1] %></span>
                                    <span class="maison-montant"><%= String.format("%.0f", total) %> €</span>
                                </div>
                                <div class="maison-barre">
                                    <div class="barre-remplissage" style="width: <%= String.format("%.0f", pourcentage) %>%;">
                                        <span class="barre-pourcentage"><%= String.format("%.0f", pourcentage) %>%</span>
                                    </div>
                                </div>
                            </div>
                        <% } %>
                    </div>
                </div>
            <% } %>

        </div>
    </main>

    <jsp:include page="/WEB-INF/vues/commun/footer.jsp" />

    <script src="${pageContext.request.contextPath}/assets/js/revenus-script.js"></script>

</body>
</html>