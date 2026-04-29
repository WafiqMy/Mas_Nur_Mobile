import 'package:flutter/material.dart';
import 'package:mas_nur_flutter/features/dashboard/dashboard_page.dart';
import 'package:mas_nur_flutter/shared/theme/app_theme.dart';
import 'package:mas_nur_flutter/shared/widgets/app_drawer.dart';
import 'package:mas_nur_flutter/shared/widgets/app_footer.dart';
import 'package:mas_nur_flutter/shared/widgets/app_header.dart';
import 'package:mas_nur_flutter/shared/widgets/swipe_page_shell.dart';

class InfaqPage extends StatelessWidget {
  const InfaqPage({super.key});
  static const routeName = '/infaq';

  @override
  Widget build(BuildContext context) {
    return PopScope(
      canPop: false,
      onPopInvokedWithResult: (didPop, result) {
        if (!didPop) {
          Navigator.pushReplacementNamed(context, DashboardPage.routeName);
        }
      },
      child: SwipePageShell(
        currentIndex: 1,
        child: Scaffold(
          backgroundColor: kColorBackground,
          drawer: const AppDrawer(),
          body: Column(
            children: [
              const AppHeader(),
              Expanded(
                child: Column(
                  children: [
                    Padding(
                      padding: const EdgeInsets.fromLTRB(16, 16, 16, 8),
                      child: Row(
                        children: [
                          const Expanded(
                            child: Text(
                              'Infaq',
                              style: TextStyle(
                                fontSize: 20,
                                fontWeight: FontWeight.bold,
                              ),
                            ),
                          ),
                        ],
                      ),
                    ),
                    const Expanded(
                      child: Center(
                        child: Column(
                          mainAxisSize: MainAxisSize.min,
                          children: [
                            Icon(
                              Icons.volunteer_activism_rounded,
                              size: 64,
                              color: Color(0xFFBDBDBD),
                            ),
                            SizedBox(height: 16),
                            Text(
                              'Fitur Infaq akan segera hadir',
                              style: TextStyle(
                                fontSize: 16,
                                color: Color(0xFF9E9E9E),
                              ),
                            ),
                          ],
                        ),
                      ),
                    ),
                  ],
                ),
              ),
              const AppFooter(currentIndex: 1),
            ],
          ),
        ),
      ),
    );
  }
}
