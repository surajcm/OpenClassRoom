<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no" />
        <meta name="description" content="" />
        <meta name="author" content="" />
        <title>OpenClassRoom - Home</title>
        <link rel="icon" type="image/x-icon" href="assets/favicon.ico" />
        <!-- Bootstrap icons-->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.5.0/font/bootstrap-icons.css" rel="stylesheet" />
        <!-- Google fonts-->
        <link rel="preconnect" href="https://fonts.gstatic.com" />
        <link href="https://fonts.googleapis.com/css2?family=Newsreader:ital,wght@0,600;1,600&amp;display=swap" rel="stylesheet" />
        <link href="https://fonts.googleapis.com/css2?family=Mulish:ital,wght@0,300;0,500;0,600;0,700;1,300;1,500;1,600;1,700&amp;display=swap" rel="stylesheet" />
        <link href="https://fonts.googleapis.com/css2?family=Kanit:ital,wght@0,400;1,400&amp;display=swap" rel="stylesheet" />
        <!-- Core theme CSS (includes Bootstrap)-->
        <link rel="stylesheet" href="/css/bootstrap-5.min.css" />
        <link rel="stylesheet" href="https://pro.fontawesome.com/releases/v5.10.0/css/all.css" integrity="sha384-AYmEC3Yw5cVb3ZcuHtOA93w35dYTsvhLPVnYs9eStHfGJvOvKxVfELGroGkvsg+p" crossorigin="anonymous"/>
        <link href="${contextPath}/css/login.css" rel="stylesheet">
    </head>
    <body>
        <nav class="navbar navbar-expand-lg navbar-light fixed-top shadow-sm" id="mainNav">
            <div class="container px-5">
                <a class="navbar-brand fw-bold" href="#page-top">OpenClassRoom</a>
                <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarResponsive" aria-controls="navbarResponsive" aria-expanded="false" aria-label="Toggle navigation">
                    Menu
                    <i class="bi-list"></i>
                </button>
                <div class="collapse navbar-collapse" id="navbarResponsive">
                    <ul class="navbar-nav ms-auto me-4 my-3 my-lg-0">
                        <li class="nav-item"><a class="nav-link me-lg-3" href="${contextPath}/plans">Plans</a></li>
                        <li class="nav-item"><a class="nav-link me-lg-3" href="${contextPath}/faq">FAQ</a></li>
                    </ul>
                </div>
            </div>
        </nav>
        <header class="masthead">
            <div class="container-fluid px-5 my-5">
                <div class="row justify-content-center">
                    <div class="col-xl-10">
                        <div class="card border-0 rounded-3 shadow-lg overflow-hidden">
                            <div class="card-body p-0">
                                <div class="row g-0">
                                    <div class="col-sm-6 d-none d-sm-block bg-image"></div>
                                    <div class="col-sm-6 p-4">
                                        <div class="text-center">
                                            <div class="h3 fw-light">Please Log In, or <a href="#">Sign Up</div>
                                        </div>
                                        <form class="form" role="form" method="post" action="${contextPath}/login" accept-charset="UTF-8" id="login-nav">
                                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                            <!-- User name Input -->
                                            <div class="form-floating mb-3">
                                                <input class="form-control" name="username" type="text" placeholder="Username" />
                                                <label for="name">Username</label>

                                            </div>
                                            <!-- Email Input -->
                                            <div class="form-floating mb-3">
                                                <input class="form-control" name="password" type="password" placeholder="password" />
                                                <label for="password">password</label>
                                            </div>
                                            <!-- Submit button -->
                                            <div class="d-grid">
                                                <button class="btn btn-primary btn-lg" type="submit">Submit</button>
                                            </div>
                                        </form>
                                        <!-- End of contact form -->
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </header>
        <!-- Footer-->
        <footer class="bg-black text-center py-5">
            <div class="container px-5">
                <div class="text-white-50 small">
                    <div class="mb-2">&copy; OpenClassRoom 2021. All Rights Reserved.</div>
                    <a href="#!">Privacy</a>
                    <span class="mx-1">&middot;</span>
                    <a href="#!">Terms</a>
                    <span class="mx-1">&middot;</span>
                    <a href="#!">FAQ</a>
                </div>
            </div>
        </footer>
        <script src="/js/popper.min.js" ></script>
        <script src="/js/bootstrap-5.min.js" ></script>
    </body>
</html>