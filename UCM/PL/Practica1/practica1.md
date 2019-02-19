---
header-includes:
- \usepackage{graphicx}
- \usepackage{tikz}
output:
    pdf_document
---

\usetikzlibrary{automata,arrows, positioning}
\renewcommand{\contentsname}{Tabla de contenidos}

\begin{titlepage}
	\begin{center}
		\includegraphics{escudo.jpg}
	\end{center}
	\centering
	{\scshape\LARGE Complutense de Madrid \par}
	\vspace{1cm}
	{\scshape\Large Práctica Procesadores de Lenguajes.\par}
	\vspace{1.5cm}
	{\huge\bfseries Primera Fase. \par}
	\vspace{2cm}
	{\Large\itshape Kyle Tan \& Lukas Häring\par}
	\vfill
	\vfill

% Bottom of the page
	{\large \today\par}
\end{titlepage}

\tableofcontents

\newpage

# 1. Introducción



# 2. Definición

## 2. 1. Clases Auxiliares.
\begin{center}
    \begin{tabular}{ | l | p{5cm} | p{3cm} |}
    \hline
    Auxiliares & Clases Léxicas & Expresión Regular \\ \hline
    Monday & 11C & 1\\ \hline
    Tuesday & 9C & 1 \\ \hline
    Wednesday & 10C & 1 \\ \hline
    \end{tabular}
\end{center}

## 2. 2. Clases Léxicas.
\begin{center}
    \begin{tabular}{ | l | p{5cm} | p{3cm} |}
    \hline
    Auxiliares & Clases Léxicas & Expresión Regular \\ \hline
    Monday & 11C & 1\\ \hline
    Tuesday & 9C & 1 \\ \hline
    Wednesday & 10C & 1 \\ \hline
    \end{tabular}
\end{center}

## 2. 3. Clases Ignorables.
\begin{center}
    \begin{tabular}{ | l | p{5cm} | p{3cm} |}
    \hline
    Auxiliares & Clases Léxicas & Expresión Regular \\ \hline
    Monday & 11C & 1\\ \hline
    Tuesday & 9C & 1 \\ \hline
    Wednesday & 10C & 1 \\ \hline
    \end{tabular}
\end{center}

\newpage

# 3. Diagrama de transiciones.

\begin{tikzpicture}[>=stealth',shorten >=0.5pt,auto,node distance=5 cm, scale = 1, transform shape]


\node[initial,state]    (A) at (0,0) {INICIO};
\node[state]  (B) at(1.5, 5.0) {AND};
\node[state,accepting]  (B1) at(4.0, 5.5) {SEP};
\node[state,accepting]  (C) at(-1.5, 5.0) {PAO};
\node[state,accepting]  (D) at(-3.0, 4.5) {DIV};
\node[state,accepting]  (M) at(-4.0, 3.5) {MUL};
\node[state,accepting]  (E) at(6.0, 0.0) {ID};
\node[state,accepting]  (F) at(0.0, 5.5)      {PCE};
\node[state,accepting] (H1) at(3.0, 4.5) {GT};
\node[state,accepting] (GE) at(5.0, 4.5) {GE};
\node[state,accepting] (H2) at(4.0, 3.5) {LS};
\node[state,accepting] (LG) at(6.0, 3.5) {LG};
\node[state,accepting] (EQ) at(5.0, 2.5) {EQ};
\node[state,accepting] (BE) at(7.0, 2.5) {BE};
\node[state] (N) at(5.5, 1.5) {N};
\node[state,accepting] (NE) at(7.5, 1.5) {NE};
\node[state,accepting] (PLS) at(5.0, -2.5) {PLS};
\node[state,accepting] (EN) at(3.5, -4.0) {INT};
\node[state,accepting] (MNS) at(1.5, -5.0) {MNS};
\node[state] (P) at(6.5, -5.0) {PNT};
\node[state] (EX) at(3.5, -7.0) {EXP};
\node[state,accepting] (DC) at(6.5, -7.5) {DEC};
\node[state] (SG) at(0.5, -7.0) {SGN};
\node[state,accepting] (XE) at(-2.5, -7.0) {EXE};

\path[->]
      (A) edge [left]  node [align=center]  {\&} (B)
      (B) edge [above]  node [align=center]  {\&} (B1)
			(A) edge [left]   node [align=center]  {$($} (C)
      (A) edge [left]  node [align=center]  {$/$} (D)
			(A) edge [left]  node [align=center]  {$* $} (M)
			(A) edge [above]  node [align=center]  {[a-z], [A-Z]} (E)
			(E) edge [loop below]  node [align=center]  {[a-z], [A-Z], [0-9], \_} (E)
			(A) edge [above left]  node [align=center]  {$)$} (F)
			(A) edge [above left]  node [align=center]  {$>$} (H1)
			(A) edge [above left]  node [align=center]  {$<$} (H2)
			(H1) edge [above]  node [align=center]  {$=$} (GE)
			(H2) edge [above]  node [align=center]  {$=$} (LG)
			(A) edge [above]  node [align=center]  {$=$} (EQ)
			(EQ) edge [above]  node [align=center]  {$=$} (BE)
			(A) edge [above]  node [align=center]  {$!$} (N)
			(N) edge [above]  node [align=center]  {$=$} (NE)
			(A) edge [right]  node [align=center]  {$+$} (PLS)
			(PLS) edge [bend left]  node [align=center]  {[0-9]} (EN)
			(A) edge [right]  node [align=center]  {$-$} (MNS)
			(MNS) edge [bend right]  node [align=center]  {[0-9]} (EN)
			(EN) edge [loop above]  node [align=center]  {[0-9]} (EN)
			(A) edge [right]  node [align=center]  {[0-9]} (EN)
			(EN) edge [right]  node [align=center]  {e|E} (EX)
			(P) edge [left]  node [align=center]  {[0-9]} (DC)
			(DC) edge [loop below]  node [align=center]  {[0-9]} (DC)
			(DC) edge [above]  node [align=center]  {e|E} (EX)
			(EX) edge [above]  node [align=center]  {+|-} (SG)
			(SG) edge [above]  node [align=center]  {[0-9]} (XE)
			(XE) edge [loop above]  node [align=center]  {[0-9]} (XE)
			(EN) edge [above]  node [align=center]  {.} (P);
\end{tikzpicture}
